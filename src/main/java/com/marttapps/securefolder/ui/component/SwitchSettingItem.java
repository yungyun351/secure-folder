package com.marttapps.securefolder.ui.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import com.marttapps.securefolder.model.constants.UiConstants;

/**
 * 開關元件
 */
public class SwitchSettingItem extends JPanel {

	private static final long serialVersionUID = 1L;

	/** 文字區域 */
	private final Box textArea;
	/** 標題文字 */
	private final JLabel titleLabel;
	/** 描述文字 */
	private final JLabel descLabel;
	/** 開關按鈕 */
	private final JToggleButton switchBtn;

	public SwitchSettingItem(String title, String desc, int titleSize, int descSize, int switchSize,
			ActionListener switchAction) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		Box content = Box.createHorizontalBox();
		content.setBorder(new EmptyBorder(20, 20, 20, 20));

		textArea = Box.createVerticalBox();

		titleLabel = new JLabel(title);
		titleLabel.setFont(new Font(UiConstants.STYLE_DEFAULT_FONT_NAME, Font.BOLD, titleSize));
		textArea.add(titleLabel);

		textArea.add(Box.createVerticalStrut(10));

		descLabel = new JLabel(desc);
		descLabel.setFont(new Font(UiConstants.STYLE_DEFAULT_FONT_NAME, Font.PLAIN, descSize));
		Color base = titleLabel.getForeground();
		descLabel.setForeground(base.darker());
		textArea.add(descLabel);

		content.add(textArea);

		Box switchArea = Box.createVerticalBox();

		switchBtn = new JToggleButton();
		switchBtn.setIcon(new SwitchIcon(false, switchSize));
		switchBtn.setSelectedIcon(new SwitchIcon(true, switchSize));
		switchBtn.setContentAreaFilled(false);
		switchBtn.setBorderPainted(false);
		switchBtn.setFocusPainted(false);
		switchBtn.addActionListener(switchAction);
		switchArea.add(switchBtn);

		content.add(switchArea);

		add(content);
	}

	/**
	 * 添加文字區域底部元件(插槽)
	 * 
	 * @param comp 元件
	 */
	public void addTextButtomComponent(JComponent comp) {
		comp.setAlignmentY(Component.TOP_ALIGNMENT);
		textArea.add(comp);
		textArea.revalidate();
		textArea.repaint();
	}

	public boolean isSwitchSelected() {
		return switchBtn.isSelected();
	}

	public void setSwitchSelected(boolean value) {
		switchBtn.setSelected(value);
	}

	public void setSwitchEnabled(boolean value) {
		switchBtn.setEnabled(value);
	}

}
