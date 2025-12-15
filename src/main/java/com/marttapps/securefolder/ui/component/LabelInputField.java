package com.marttapps.securefolder.ui.component;

import javax.swing.JTextField;

import com.formdev.flatlaf.FlatClientProperties;

/**
 * 輸入框欄位元件
 */
public class LabelInputField extends LabelTextField {

	private static final long serialVersionUID = 1L;

	public LabelInputField(int width, String labelText, int labelSize, int errorSize, boolean showClearButton) {
		this(width, labelText, labelSize, errorSize, "", showClearButton);
	}

	public LabelInputField(int width, String labelText, int labelSize, int errorSize, String errorText,
			boolean showClearButton) {
		super(width, labelText, labelSize, errorSize, errorText);
		if (showClearButton) {
			input.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
		}
	}

	@Override
	protected JTextField createInput() {
		return new JTextField();
	}

}
