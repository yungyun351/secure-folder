package com.marttapps.securefolder.ui.component;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.formdev.flatlaf.FlatClientProperties;

/**
 * 密碼欄位元件
 */
public class LabelPasswordField extends LabelTextField {

	private static final long serialVersionUID = 1L;

	private static final String ICON_SHOW = "👁";
	private static final String ICON_HIDE = "❌";

	private final JButton toggleBtn;
	private boolean showing = false;

	public LabelPasswordField(int width, String labelText, int labelSize, int errorSize) {
		this(width, labelText, labelSize, errorSize, "");
	}

	public LabelPasswordField(int width, String labelText, int labelSize, int errorSize, String errorText) {
		super(width, labelText, labelSize, errorSize, errorText);
		toggleBtn = new JButton(ICON_SHOW);
		int sizePx = labelSize * 96 / 72;
		Dimension btnSize = new Dimension(sizePx, sizePx);
		toggleBtn.setPreferredSize(btnSize);
		toggleBtn.setMinimumSize(btnSize);
		toggleBtn.setMaximumSize(btnSize);
		toggleBtn.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_BORDERLESS);
		toggleBtn.addActionListener(e -> togglePassword());
		addRightComponent(toggleBtn);
	}

	@Override
	protected JTextField createInput() {
		return new JPasswordField();
	}

	private void togglePassword() {
		showing = !showing;
		JPasswordField pf = (JPasswordField) input;
		if (showing) {
			pf.setEchoChar((char) 0);
			toggleBtn.setText(ICON_HIDE);
		} else {
			pf.setEchoChar('•');
			toggleBtn.setText(ICON_SHOW);
		}
	}

}
