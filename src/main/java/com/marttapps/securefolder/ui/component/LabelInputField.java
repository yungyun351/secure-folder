package com.marttapps.securefolder.ui.component;

import javax.swing.JTextField;

public class LabelInputField extends LabelTextField {

	private static final long serialVersionUID = 1L;

	public LabelInputField(int width, String labelText, int labelSize, int errorSize) {
		this(width, labelText, labelSize, errorSize, "");
	}

	public LabelInputField(int width, String labelText, int labelSize, int errorSize, String errorText) {
		super(width, labelText, labelSize, errorSize, errorText);
	}

	@Override
	protected JTextField createInput() {
		return new JTextField();
	}

}
