package com.marttapps.securefolder.ui.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;
import javax.swing.UIManager;

/**
 * 開關元件圖標
 */
public class SwitchIcon implements Icon {

	private static final Color COLOR_BACKGROUND = UIManager.getColor("CheckBox.icon.background");
	private static final Color COLOR_BACKGROUND_SELECTED = UIManager.getColor("CheckBox.icon.selectedBackground");
	private static final Color COLOR_KNOB = Color.WHITE;
	private static final Color COLOR_KNOB_SELECTED = Color.DARK_GRAY;
	private static final Color COLOR_BORDER = UIManager.getColor("CheckBox.icon.borderColor");

	/** 是否選取 */
	private final boolean selected;
	/** 寬 */
	private final int width;
	/** 高 */
	private final int height;
	/** 邊框大小 */
	private final int borderSize;
	/** 滑塊大小 */
	private final int knobSize;
	/** 滑塊內距 */
	private final int knobPadding;
	/** 圓角半徑 */
	private final int arc;

	public SwitchIcon(boolean selected, int size) {
		super();
		this.selected = selected;
		this.borderSize = Math.round(size / 8f);
		this.knobPadding = Math.round(size / 8f);
		this.knobSize = size - borderSize - knobPadding * 2;
		this.width = borderSize + knobPadding * 2 + knobSize * 2;
		this.height = size;
		this.arc = height;
	}

	@Override
	public int getIconWidth() {
		return width;
	}

	@Override
	public int getIconHeight() {
		return height;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.translate(x, y);

		// 背景
		g2.setColor(selected ? COLOR_BACKGROUND_SELECTED : COLOR_BACKGROUND);
		g2.fillRoundRect(0, 0, width, height, arc, arc);

		// 邊框
		g2.setStroke(new BasicStroke(borderSize));
		g2.setColor(COLOR_BORDER);
		g2.drawRoundRect(0, 0, width, height, arc, arc);

		// 滑塊
		int inset = borderSize / 2 + knobPadding;
		int knobX = selected ? width - inset - knobSize : inset;
		int knobY = inset;

		g2.setColor(selected ? COLOR_KNOB_SELECTED : COLOR_KNOB);
		g2.fillRoundRect(knobX, knobY, knobSize, knobSize, arc, arc);

		g2.dispose();
	}

}
