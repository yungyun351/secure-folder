package com.marttapps.securefolder.ui.feature;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import com.marttapps.securefolder.config.Routes;
import com.marttapps.securefolder.model.constants.UiConstants;
import com.marttapps.swingrouter.Router;

public class AppFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public AppFrame(Router router) {
		super("Secure Folder");
		render(router);
	}

	private void render(Router router) {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(550, 500);
		setLayout(new BorderLayout(10, 10));
		setResizable(true);
		setLocationRelativeTo(null);

		ImageIcon icon = new ImageIcon(getClass().getResource("/img/icon.png"));
		setIconImage(icon.getImage());

		Box content = Box.createVerticalBox();

		JTabbedPane tabs = new JTabbedPane();
		tabs.setFont(new Font(UiConstants.STYLE_DEFAULT_FONT_NAME, Font.BOLD, UiConstants.STYLE_DEFAULT_FONT_SIZE));
		tabs.addTab("偏好設定", null);
		tabs.addTab("加密", null);
		tabs.addTab("解密", null);
		tabs.addChangeListener(e -> {
			int index = tabs.getSelectedIndex();
			if (index == 0)
				router.navigate(Routes.PREFERENCES);
			else if (index == 1)
				router.navigate(Routes.ENCRYPT_FILE);
			else if (index == 2)
				router.navigate(Routes.DECRYPT_FILE);
		});
		content.add(tabs);

		content.add(Box.createVerticalStrut(20));

		JPanel screenRow = new JPanel();
		screenRow.setLayout(new BoxLayout(screenRow, BoxLayout.X_AXIS));
		screenRow.add(router.getRouterView());
		content.add(screenRow);

		add(content);

		setVisible(true);
	}

}
