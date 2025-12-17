package com.marttapps.securefolder.ui.feature;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.marttapps.securefolder.config.Routes;
import com.marttapps.securefolder.model.constants.UiConstants;
import com.marttapps.securefolder.util.ApplicationPropUtil;
import com.marttapps.swingrouter.Router;

public class AppFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public AppFrame(Router router) {
		super(ApplicationPropUtil.get("application", "name"));
		render(router);
	}

	private void render(Router router) {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(600, 500);
		setLayout(new BorderLayout(10, 10));
		setResizable(true);
		setLocationRelativeTo(null);

		ImageIcon icon = new ImageIcon(getClass().getResource("/img/icon.png"));
		setIconImage(icon.getImage());

		Box content = Box.createHorizontalBox();

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setTabPlacement(SwingConstants.LEFT);
		List<Routes> nav = List.of(Routes.PREFERENCES, Routes.ENCRYPT_FILE, Routes.DECRYPT_FILE);
		for (int index = 0; index < nav.size(); index++) {
			Routes route = nav.get(index);

			// 先建立空的，再用index位置替換成icon + text
			tabbedPane.addTab("", null);

			JPanel tab = new JPanel(new BorderLayout());
			tab.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			tab.setOpaque(false);
			ImageIcon routeIcon = new ImageIcon(getClass().getResource(route.getIconPath()));
			JLabel label = new JLabel(route.getName(), routeIcon, SwingConstants.CENTER);
			label.setFont(
					new Font(UiConstants.STYLE_DEFAULT_FONT_NAME, Font.BOLD, UiConstants.STYLE_DEFAULT_FONT_SIZE));
			label.setHorizontalTextPosition(SwingConstants.CENTER);
			label.setVerticalTextPosition(SwingConstants.BOTTOM);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			tab.add(label, BorderLayout.CENTER);
			tabbedPane.setTabComponentAt(index, tab);
		}
		tabbedPane.addChangeListener(e -> router.navigate(nav.get(tabbedPane.getSelectedIndex())));
		content.add(tabbedPane);

		content.add(Box.createVerticalStrut(20));

		JPanel screenRow = new JPanel();
		screenRow.setLayout(new BoxLayout(screenRow, BoxLayout.X_AXIS));
		screenRow.add(router.getRouterView());
		content.add(screenRow);

		add(content);

		setVisible(true);
	}

}
