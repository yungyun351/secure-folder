package com.marttapps.securefolder.ui.component;

import java.awt.Dimension;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class LabelFileField extends LabelTextField {

	private static final long serialVersionUID = 1L;

	private final JButton fileBtn;

	public LabelFileField(int width, String labelText, int labelSize, int errorSize, int fileSelectionMode) {
		this(width, labelText, labelSize, errorSize, "", fileSelectionMode);
	}

	public LabelFileField(int width, String labelText, int labelSize, int errorSize, String errorText,
			int fileSelectionMode) {
		super(width, labelText, labelSize, errorSize, errorText);
		fileBtn = new JButton(UIManager.getIcon("FileView.directoryIcon"));
		int sizePx = labelSize * 96 / 72;
		Dimension btnSize = new Dimension(sizePx, sizePx);
		fileBtn.setPreferredSize(btnSize);
		fileBtn.setMinimumSize(btnSize);
		fileBtn.setMaximumSize(btnSize);
		fileBtn.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(fileSelectionMode);
			int result = fileChooser.showOpenDialog(null);
			if (result != JFileChooser.APPROVE_OPTION) {
				return;
			}
			File folder = fileChooser.getSelectedFile();
			input.setText(folder.getAbsolutePath());
		});
		this.addRightComponent(fileBtn);
	}

	@Override
	protected JTextField createInput() {
		return new JTextField();
	}

}
