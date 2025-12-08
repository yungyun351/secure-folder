package com.marttapps.securefolder.ui.feature;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
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
import com.marttapps.securefolder.model.constants.UiConstants;
import com.marttapps.swingrouter.RoutePanel;
import com.marttapps.swingrouter.Router;

public class DecryptFileRoutePanel extends RoutePanel {

	private static final long serialVersionUID = 1L;

	/** 資料夾欄位 */
	private LabelTextField dirField;
	/** 密碼欄位 */
	private LabelTextField pwdField;
	/** 開啟資料夾欄位 */
	private JCheckBox openDirCheckbox;
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
				JFileChooser.DIRECTORIES_ONLY);
		content.add(dirField);

		pwdField = new LabelPasswordField(fieldWidth, "密碼:", fieldLabelSize, fieldErrorSize, errorText);
		content.add(pwdField);

		JPanel openDirPanel = new JPanel();
		openDirCheckbox = new JCheckBox("完成後開啟資料夾");
		openDirCheckbox.setFont(
				new Font(UiConstants.STYLE_DEFAULT_FONT_NAME, Font.PLAIN, UiConstants.STYLE_DEFAULT_FONT_SIZE));
		openDirCheckbox.setSelected(true);
		openDirPanel.add(openDirCheckbox);

		if (Desktop.isDesktopSupported()) {
			openDirCheckbox.setSelected(true);
		} else {
			openDirCheckbox.setSelected(false);
			openDirCheckbox.setEnabled(false);
			JLabel hint = new JLabel("系統不支援");
			hint.setFont(new Font(UiConstants.STYLE_DEFAULT_FONT_NAME, Font.BOLD, hintSize));
			hint.setForeground(Color.RED);
			openDirPanel.add(hint);
		}

		content.add(openDirPanel);

		JPanel buttonPanel = new JPanel();
		JButton execBtn = new JButton("執行解密");
		execBtn.setFont(new Font(UiConstants.STYLE_DEFAULT_FONT_NAME, Font.BOLD, UiConstants.STYLE_DEFAULT_FONT_SIZE));
		execBtn.addActionListener(this::exec);
		buttonPanel.add(execBtn);
		content.add(buttonPanel);
		add(content);

		Box footer = Box.createVerticalBox();
		footer.setBorder(new EmptyBorder(0, 20, 20, 20));

		progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		footer.add(progressBar);

		logArea = new JTextArea(8, 40);
		logArea.setEditable(false);
		logArea.setLineWrap(true);
		logArea.setWrapStyleWord(true);

		JPanel consolePanel = new JPanel(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(logArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		consolePanel.add(scrollPane, BorderLayout.CENTER);
		footer.add(consolePanel);

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

		// 確定執行後就清空密碼
		pwdField.clearInput();

		worker = new SecureProcessWorker(false, dirPath, pwd.toCharArray(), openDirCheckbox.isSelected(), progressBar,
				logArea);
		worker.execute();
	}

}
