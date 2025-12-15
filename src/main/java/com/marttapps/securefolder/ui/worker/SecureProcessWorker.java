package com.marttapps.securefolder.ui.worker;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.marttapps.securefolder.model.bean.Preferences;
import com.marttapps.securefolder.model.listener.EncFileProgressListener;
import com.marttapps.securefolder.service.EncFileService;
import com.marttapps.securefolder.service.PreferencesService;
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
	/** 執行加密按鈕 */
	private final JButton execBtn;
	/** 取消按鈕 */
	private final JButton cancelBtn;
	/** 進度條元件 */
	private final JProgressBar progressBar;
	/** 日誌元件 */
	private final JTextArea logArea;

	/** 開始時間 */
	private long workerStartTime;

	/**
	 * 建構子
	 * 
	 * @param encryptMode 是否為加密模式
	 * @param dirPath     目錄
	 * @param password    密碼
	 * @param execBtn     執行按鈕
	 * @param cancelBtn   取消按鈕
	 * @param progressBar 進度條元件
	 * @param logArea     日誌元件
	 */
	public SecureProcessWorker(boolean encryptMode, Path dirPath, char[] password, JButton execBtn, JButton cancelBtn,
			JProgressBar progressBar, JTextArea logArea) {
		this.encryptMode = encryptMode;
		this.dirPath = dirPath;
		this.password = password;
		this.execBtn = execBtn;
		this.cancelBtn = cancelBtn;
		this.progressBar = progressBar;
		this.logArea = logArea;
	}

	@Override
	protected Void doInBackground() {
		workerStartTime = System.currentTimeMillis();
		EncFileProgressListener listener = new EncFileProgressListener() {

			/** 檔案總數 */
			private int totalFiles;
			/** 開始時間 */
			private long fileStartTime;

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
				fileStartTime = System.currentTimeMillis();
				SwingUtilities.invokeLater(() -> {
					String log = String.format("(%d/%d) 正在處理： %s ... ", index + 1, this.totalFiles, file.getFileName());
					logArea.append(log);
					logArea.setCaretPosition(logArea.getDocument().getLength());
				});
				publish(index);
			}

			@Override
			public void onFileDone(int index, Path file, boolean success, Exception exception) {
				String durationText = formatDuration(fileStartTime, System.currentTimeMillis());
				SwingUtilities.invokeLater(() -> {
					if (success) {
						logArea.append("成功。");
					} else if (exception instanceof IOException) {
						logArea.append("檔案讀取失敗。");
					} else if (exception instanceof BadPaddingException) {
						logArea.append("失敗，密碼錯誤。");
					}
					logArea.append(durationText);
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
				// future extension
			}
		};

		try {
			if (encryptMode) {
				EncFileService.INSTANCE.encryptFolder(dirPath, password, listener);
			} else {
				EncFileService.INSTANCE.decryptFolder(dirPath, password, listener);
			}
		} catch (IOException e) {
			DialogUtil.showErrorDialog("檔案處理失敗。");
		}
		return null;
	}

	@Override
	protected void process(List<Integer> chunks) {
		SwingUtilities.invokeLater(() -> progressBar.setValue(chunks.get(chunks.size() - 1)));
	}

	@Override
	protected void done() {
		// 執行偏好設定
		Preferences prefs = null;
		try {
			prefs = PreferencesService.INSTANCE.load();
		} catch (URISyntaxException ignore) {
			// ignore
		}
		if (prefs != null) {
			if (prefs.isRememberLastFolder()) {
				prefs.setLastFolder(dirPath.toAbsolutePath().toString());
				try {
					PreferencesService.INSTANCE.save(prefs);
				} catch (URISyntaxException | IOException e) {
					DialogUtil.showErrorDialog("偏好設定檔讀寫失敗。");
				}
			}

			if (prefs.isAutoOpenDir()) {
				try {
					Desktop.getDesktop().open(dirPath.toFile());
				} catch (IOException e) {
					DialogUtil.showErrorDialog("目錄開啟失敗。");
				}
			}
		}

		String durationText = formatDuration(workerStartTime, System.currentTimeMillis());
		SwingUtilities.invokeLater(() -> {
			execBtn.setVisible(true);
			cancelBtn.setVisible(false);
			progressBar.setValue(progressBar.getValue() + 1);
			logArea.append("完成。");
			logArea.append(durationText);
			logArea.append(System.lineSeparator());
			logArea.setCaretPosition(logArea.getDocument().getLength());
		});
	}

	/**
	 * 格式化執行時間文字(hr min s ms)
	 * 
	 * @param startTimeMs 開始時間(ms)
	 * @param endTimeMs   結束時間(ms)
	 * @return 執行時間文字(hr min s ms)
	 */
	private String formatDuration(long startTimeMs, long endTimeMs) {
		long ms = endTimeMs - startTimeMs;
		long hours = ms / 3600000;
		ms %= 3600000;
		long minutes = ms / 60000;
		ms %= 60000;
		long seconds = ms / 1000;
		ms %= 1000;

		StringBuilder costText = new StringBuilder("(");
		if (hours > 0) {
			costText.append(hours).append("hr ");
		}
		if (minutes > 0 || hours > 0) {
			costText.append(minutes).append("min ");
		}
		if (seconds > 0 || minutes > 0 || hours > 0) {
			costText.append(seconds).append("s ");
		}
		costText.append(ms).append("ms");
		costText.append(")");
		return costText.toString();
	}

}
