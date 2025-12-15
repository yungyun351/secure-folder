package com.marttapps.securefolder.ui.component;

import java.awt.Dimension;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatClientProperties;
import com.marttapps.securefolder.model.constants.UiConstants;

/**
 * 檔案選取欄位元件
 */
public class LabelFileField extends LabelTextField {

	private static final long serialVersionUID = 1L;

	private final JButton fileBtn;

	public LabelFileField(int width, String labelText, int labelSize, int errorSize, int fileSelectionMode,
			boolean showClearButton) {
		this(width, labelText, labelSize, errorSize, "", fileSelectionMode, showClearButton);
	}

	public LabelFileField(int width, String labelText, int labelSize, int errorSize, String errorText,
			int fileSelectionMode, boolean showClearButton) {
		super(width, labelText, labelSize, errorSize, errorText);
		fileBtn = new JButton(UIManager.getIcon(UiConstants.ICON_DIRECTORY));
		int sizePx = labelSize * 96 / 72;
		Dimension btnSize = new Dimension(sizePx, sizePx);
		fileBtn.setPreferredSize(btnSize);
		fileBtn.setMinimumSize(btnSize);
		fileBtn.setMaximumSize(btnSize);
		fileBtn.setFocusable(false);
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
		if (showClearButton) {
			input.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
		}
	}

	@Override
	protected JTextField createInput() {
		return new JTextField();
	}

}
