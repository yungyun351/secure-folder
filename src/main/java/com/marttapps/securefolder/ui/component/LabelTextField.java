package com.marttapps.securefolder.ui.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.formdev.flatlaf.FlatClientProperties;
import com.marttapps.securefolder.model.constants.UiConstants;

/**
 * 欄位元件的包裝類別
 */
public abstract class LabelTextField extends JPanel {

	private static final long serialVersionUID = 1L;

	/** 主欄位區域 */
	protected Box fieldBox;
	/** 標籤 */
	protected JLabel label;
	/** 輸入框 */
	protected JTextField input;
	/** 錯誤標籤 */
	protected JLabel errorLabel;
	/** 錯誤訊息 */
	protected String errorText;

	protected LabelTextField(int width, String labelText, int labelSize, int errorSize, String errorText) {
		this.errorText = errorText;

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		fieldBox = Box.createHorizontalBox();
		label = new JLabel(labelText);
		label.setFont(new Font(UiConstants.STYLE_DEFAULT_FONT_NAME, Font.BOLD, labelSize));
		label.setAlignmentY(Component.TOP_ALIGNMENT);
		fieldBox.add(label);

		fieldBox.add(Box.createHorizontalStrut(10));

		Box inputBox = Box.createVerticalBox();
		inputBox.setAlignmentY(Component.TOP_ALIGNMENT);

		input = createInput();
		input.setFont(new Font(UiConstants.STYLE_DEFAULT_FONT_NAME, Font.PLAIN, labelSize - 6));
		input.setAlignmentX(Component.LEFT_ALIGNMENT);
		inputBox.add(input);

		errorLabel = new JLabel();
		errorLabel.setFont(new Font(UiConstants.STYLE_DEFAULT_FONT_NAME, Font.PLAIN, errorSize));
		errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		errorLabel.setForeground(Color.RED);
		errorLabel.setPreferredSize(new Dimension(0, errorSize + 2));
		inputBox.add(errorLabel);

		fieldBox.add(inputBox);

		add(fieldBox);

		Dimension dimension = new Dimension(width, getPreferredSize().height);
		setPreferredSize(dimension);
		setMaximumSize(dimension);
		setMinimumSize(dimension);
	}

	protected abstract JTextField createInput();

	/**
	 * 添加右側元件(插槽)
	 * 
	 * @param comp 元件
	 */
	public void addRightComponent(JComponent comp) {
		comp.setAlignmentY(Component.TOP_ALIGNMENT);
		fieldBox.add(comp);
		fieldBox.revalidate();
		fieldBox.repaint();
		input.repaint();
	}

	/**
	 * 取得輸入內容
	 * 
	 * @return 輸入內容
	 */
	public String getText() {
		return input.getText();
	}

	/**
	 * 設定輸入內容
	 * 
	 * @param text 內容
	 */
	public void setText(String text) {
		input.setText(text);
	}

	/**
	 * 顯示錯誤訊息
	 */
	public void showError() {
		errorLabel.setText(errorText);
		input.putClientProperty(FlatClientProperties.OUTLINE, FlatClientProperties.OUTLINE_ERROR);
	}

	/**
	 * 設定錯誤訊息並顯示
	 * 
	 * @param message 錯誤訊息
	 */
	public void showError(String message) {
		errorLabel.setText(message);
		input.putClientProperty(FlatClientProperties.OUTLINE, FlatClientProperties.OUTLINE_ERROR);
	}

	/**
	 * 隱藏錯誤訊息
	 */
	public void hideError() {
		errorLabel.setText("");
		input.putClientProperty(FlatClientProperties.OUTLINE, null);
	}

	/**
	 * 取得JTextField
	 * 
	 * @return JTextField
	 */
	public JTextField getInputField() {
		return input;
	}

	/**
	 * 清空JTextField
	 */
	public void clearInput() {
		input.setText("");
	}
}
