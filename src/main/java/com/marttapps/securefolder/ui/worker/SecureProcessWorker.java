package com.marttapps.securefolder.ui.worker;

import java.awt.Desktop;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.marttapps.securefolder.model.listener.EncFileProgressListener;
import com.marttapps.securefolder.service.EncFileService;
import com.marttapps.securefolder.util.DialogUtil;

/**
 * 加解密背景任務
 */
public class SecureProcessWorker extends SwingWorker<Void, Integer> {

	/** 是否為加密模式 */
	private final boolean encryptMode;
	/** 目錄 */
	private final Path dirPath;
	/** 密碼 */
	private final char[] password;
	/** 是否在完成後自動開啟目錄 */
	private final boolean autoOpenDir;
	/** 執行加密按鈕 */
	private JButton execBtn;
	/** 取消按鈕 */
	private JButton cancelBtn;
	/** 進度條元件 */
	private final JProgressBar progressBar;
	/** 日誌元件 */
	private final JTextArea logArea;

	/**
	 * 建構子
	 * 
	 * @param encryptMode 是否為加密模式
	 * @param dirPath     目錄
	 * @param password    密碼
	 * @param autoOpenDir 是否在完成後自動開啟目錄
	 * @param progressBar 進度條元件
	 * @param logArea     日誌元件
	 */
	public SecureProcessWorker(boolean encryptMode, Path dirPath, char[] password, boolean autoOpenDir, JButton execBtn,
			JButton cancelBtn, JProgressBar progressBar, JTextArea logArea) {
		this.encryptMode = encryptMode;
		this.dirPath = dirPath;
		this.password = password;
		this.autoOpenDir = autoOpenDir;
		this.execBtn = execBtn;
		this.cancelBtn = cancelBtn;
		this.progressBar = progressBar;
		this.logArea = logArea;
	}

	@Override
	protected Void doInBackground() {
		EncFileProgressListener listener = new EncFileProgressListener() {

			/** 檔案總數 */
			private int totalFiles;
			/** 開始時間 */
			private long startTime;

			@Override
			public void onStart(int totalFiles) {
				this.totalFiles = totalFiles;
				SwingUtilities.invokeLater(() -> {
					progressBar.setMaximum(totalFiles);
					progressBar.setValue(0);
				});
			}

			@Override
			public void onFileStart(int index, Path file) {
				startTime = System.currentTimeMillis();
				SwingUtilities.invokeLater(() -> {
					String log = String.format("(%d/%d) 正在處理： %s ... ", index + 1, this.totalFiles, file.getFileName());
					logArea.append(log);
					logArea.setCaretPosition(logArea.getDocument().getLength());
				});
				publish(index);
			}

			@Override
			public void onFileDone(int index, Path file, boolean success, Exception exception) {
				long cost = System.currentTimeMillis() - startTime;
				SwingUtilities.invokeLater(() -> {
					if (success) {
						logArea.append("成功。");
					} else if (exception instanceof IOException) {
						logArea.append("檔案讀取失敗。");
					} else if (exception instanceof BadPaddingException) {
						logArea.append("失敗，密碼錯誤。");
					}
					logArea.append(String.format("(%d ms)", cost));
					logArea.append(System.lineSeparator());
					logArea.setCaretPosition(logArea.getDocument().getLength());
				});
			}

			@Override
			public boolean isCancelled() {
				return SecureProcessWorker.this.isCancelled();
			}

			@Override
			public void onCompleted() {
				SwingUtilities.invokeLater(() -> {
					logArea.append("完成。");
					logArea.append(System.lineSeparator());
					logArea.setCaretPosition(logArea.getDocument().getLength());
				});
			}
		};

		try {
			if (encryptMode) {
				EncFileService.INSTANCE.encryptFolder(dirPath, password, listener);
			} else {
				EncFileService.INSTANCE.decryptFolder(dirPath, password, listener);
			}
		} catch (IOException e) {
			DialogUtil.showErrorDialog("檔案處理失敗");
		}
		return null;
	}

	@Override
	protected void process(List<Integer> chunks) {
		progressBar.setValue(chunks.get(chunks.size() - 1));
	}

	@Override
	protected void done() {
		execBtn.setVisible(true);
		cancelBtn.setVisible(false);
		progressBar.setValue(progressBar.getValue() + 1);

		if (isCancelled()) {
			DialogUtil.showInfoDialog("操作已取消");
			return;
		}

		if (autoOpenDir) {
			try {
				Desktop.getDesktop().open(dirPath.toFile());
			} catch (IOException e) {
				DialogUtil.showErrorDialog("目錄開啟失敗");
			}
		} else {
			DialogUtil.showInfoDialog("操作完成");
		}

	}

}
