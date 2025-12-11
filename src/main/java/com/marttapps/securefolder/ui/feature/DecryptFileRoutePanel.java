package com.marttapps.securefolder.ui.feature;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.stream.Stream;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import com.marttapps.securefolder.ui.component.LabelFileField;
import com.marttapps.securefolder.ui.component.LabelPasswordField;
import com.marttapps.securefolder.ui.component.LabelTextField;
import com.marttapps.securefolder.ui.worker.SecureProcessWorker;
import com.marttapps.securefolder.util.DialogUtil;
import com.marttapps.securefolder.model.constants.LimitConstants;
import com.marttapps.securefolder.model.constants.UiConstants;
import com.marttapps.securefolder.service.EncFileService;
import com.marttapps.swingrouter.RoutePanel;
import com.marttapps.swingrouter.Router;

/**
 * 解密功能
 */
public class DecryptFileRoutePanel extends RoutePanel {

	private static final long serialVersionUID = 1L;

	/** 資料夾欄位 */
	private LabelTextField dirField;
	/** 密碼欄位 */
	private LabelTextField pwdField;
	/** 開啟資料夾欄位 */
	private JCheckBox openDirCheckbox;
	/** 執行解密按鈕 */
	private JButton execBtn;
	/** 取消按鈕 */
	private JButton cancelBtn;
	/** 進度條 */
	private JProgressBar progressBar;
	/** 日誌 */
	private JTextArea logArea;
	/** 加解密背景任務 */
	private transient SecureProcessWorker worker;

	public DecryptFileRoutePanel(Router router) {
		super(router);
		render();
	}

	private void render() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		int hintSize = 12;
		int fieldWidth = 400;
		int fieldLabelSize = 20;
		int fieldErrorSize = 14;
		String errorText = "錯誤";

		Box content = Box.createVerticalBox();
		dirField = new LabelFileField(fieldWidth, "資料夾:", fieldLabelSize, fieldErrorSize, errorText,
				JFileChooser.DIRECTORIES_ONLY, true);
		content.add(dirField);

		pwdField = new LabelPasswordField(fieldWidth, "密碼:", fieldLabelSize, fieldErrorSize, errorText, true);
		content.add(pwdField);

		Box openDirRow = Box.createHorizontalBox();
		openDirRow.setBorder(new EmptyBorder(0, 0, 10, 0));

		openDirCheckbox = new JCheckBox("完成後開啟資料夾");
		openDirCheckbox.setFont(
				new Font(UiConstants.STYLE_DEFAULT_FONT_NAME, Font.PLAIN, UiConstants.STYLE_DEFAULT_FONT_SIZE));
		openDirCheckbox.setSelected(true);
		openDirRow.add(openDirCheckbox);

		if (Desktop.isDesktopSupported()) {
			openDirCheckbox.setSelected(true);
		} else {
			openDirCheckbox.setSelected(false);
			openDirCheckbox.setEnabled(false);
			JLabel hint = new JLabel("系統不支援");
			hint.setFont(new Font(UiConstants.STYLE_DEFAULT_FONT_NAME, Font.BOLD, hintSize));
			hint.setForeground(Color.RED);
			openDirRow.add(hint);
		}

		content.add(openDirRow);

		Box buttonRow = Box.createHorizontalBox();
		buttonRow.setBorder(new EmptyBorder(0, 0, 10, 0));

		execBtn = new JButton("執行解密");
		execBtn.setFont(new Font(UiConstants.STYLE_DEFAULT_FONT_NAME, Font.BOLD, UiConstants.STYLE_DEFAULT_FONT_SIZE));
		execBtn.addActionListener(this::exec);
		buttonRow.add(execBtn);

		cancelBtn = new JButton("取消");
		cancelBtn
				.setFont(new Font(UiConstants.STYLE_DEFAULT_FONT_NAME, Font.BOLD, UiConstants.STYLE_DEFAULT_FONT_SIZE));
		cancelBtn.setBackground(Color.RED);
		cancelBtn.addActionListener(this::cancel);
		cancelBtn.setVisible(false);
		buttonRow.add(cancelBtn);

		content.add(buttonRow);
		add(content);

		Box footer = Box.createVerticalBox();
		footer.setBorder(new EmptyBorder(0, 10, 10, 10));

		progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		footer.add(progressBar);

		JPanel console = new JPanel(new BorderLayout());
		logArea = new JTextArea(8, 40);
		logArea.setEditable(false);
		logArea.setLineWrap(true);
		logArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(logArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
		console.add(scrollPane, BorderLayout.CENTER);
		footer.add(console);

		add(footer);
	}

	/**
	 * 執行加密
	 * 
	 * @param e
	 */
	private void exec(ActionEvent e) {
		dirField.hideError();
		pwdField.hideError();

		String dir = dirField.getText();
		Path dirPath = Path.of(dir);
		if (dir == null || dir.isBlank() || Files.notExists(dirPath)) {
			dirField.showError();
			return;
		}

		String pwd = pwdField.getText();
		if (pwd == null || pwd.isBlank()) {
			pwdField.showError();
			return;
		}

		// 檢查要處理的檔案大小
		if (!confirmLargeFiles(dirPath)) {
			return;
		}

		// 確定執行後就清空密碼
		pwdField.clearInput();

		worker = new SecureProcessWorker(false, dirPath, pwd.toCharArray(), openDirCheckbox.isSelected(), execBtn,
				cancelBtn, progressBar, logArea);
		worker.execute();

		execBtn.setVisible(false);
		cancelBtn.setVisible(true);
	}

	/**
	 * 檢查資料夾內待處理的檔案總大小
	 * 
	 * @param dirPath 資料夾路徑
	 * @return 是否繼續執行
	 */
	private boolean confirmLargeFiles(Path dirPath) {
		long totalSize = 0;
		boolean exceedLimit = false;
		try (Stream<Path> paths = Files.walk(dirPath)) {
			Iterator<Path> iterator = paths.iterator();
			while (iterator.hasNext()) {
				Path file = iterator.next();
				if (!Files.isRegularFile(file) || EncFileService.INSTANCE.isNotEncryptedFile(file)) {
					continue;
				}

				try {
					totalSize += Files.size(file);
				} catch (IOException ignored) {
					continue;
				}

				if (totalSize >= LimitConstants.SECURE_FOLDER_SIZE_LIMIT) {
					exceedLimit = true;
					break;
				}
			}
		} catch (IOException ex) {
			DialogUtil.showErrorDialog("無法讀取資料夾");
			return false;
		}

		if (exceedLimit) {
			DialogUtil.showWarnDialog("即將處理的檔案總大小超過100GB，請將檔案分批處理。");
			return false;
		}

		if (totalSize > LimitConstants.SECURE_FOLDER_SIZE_WARNING) {
			double sizeGB = totalSize / (1024.0 * 1024.0 * 1024.0);
			String msg = String.format("即將處理的檔案總大小為 %.2fGB。\n" + "處理時間可能會較長，請問要繼續嗎？", sizeGB);
			int result = DialogUtil.showConfirmDialog(msg);
			return result == JOptionPane.YES_OPTION;
		}

		return true;
	}

	/**
	 * 取消
	 * 
	 * @param e
	 */
	private void cancel(ActionEvent e) {
		if (worker == null)
			return;

		if (!worker.isCancelled()) {
			worker.cancel(true);
		}
		cancelBtn.setVisible(false);
		execBtn.setVisible(true);
	}

}
