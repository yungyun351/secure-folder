package com.marttapps.securefolder.ui.component;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.formdev.flatlaf.FlatClientProperties;

/**
 * 密碼欄位元件
 */
public class LabelPasswordField extends LabelTextField {

	private static final long serialVersionUID = 1L;

	public LabelPasswordField(int width, String labelText, int labelSize, int errorSize, boolean showRevealButton) {
		this(width, labelText, labelSize, errorSize, "", showRevealButton);
	}

	public LabelPasswordField(int width, String labelText, int labelSize, int errorSize, String errorText,
			boolean showRevealButton) {
		super(width, labelText, labelSize, errorSize, errorText);
		if (showRevealButton) {
			input.putClientProperty(FlatClientProperties.STYLE, "showRevealButton: true");
		}
	}

	@Override
	protected JTextField createInput() {
		return new JPasswordField();
	}

}
