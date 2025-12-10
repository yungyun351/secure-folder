package com.marttapps.securefolder.util;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.marttapps.securefolder.model.constants.UiConstants;

public class DialogUtil {

	private DialogUtil() {
		throw new UnsupportedOperationException("Class should not be instantiated");
	}

	public static void showInfoDialog(String msg) {
		JOptionPane.showMessageDialog(null, msg, "提示訊息", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void showWarnDialog(String msg) {
		JOptionPane.showMessageDialog(null, msg, "警告訊息", JOptionPane.WARNING_MESSAGE);
	}

	public static void showErrorDialog(String msg) {
		JOptionPane.showMessageDialog(null, msg, "錯誤訊息", JOptionPane.ERROR_MESSAGE);
	}

	public static int showConfirmDialog(String msg) {
		JLabel messageLabel = new JLabel(msg);
		messageLabel.setFont(new Font(UiConstants.STYLE_DEFAULT_FONT_NAME, Font.BOLD, UiConstants.STYLE_DEFAULT_FONT_SIZE));
//        messageLabel.setFont(new Font("Serif", Font.BOLD, 20));
		return JOptionPane.showConfirmDialog(null, messageLabel, "確認訊息", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	}

}
