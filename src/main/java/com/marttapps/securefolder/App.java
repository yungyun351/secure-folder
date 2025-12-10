package com.marttapps.securefolder;

import java.util.Locale;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.IntelliJTheme;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.marttapps.securefolder.ui.feature.AppFrame;
import com.marttapps.securefolder.util.DialogUtil;
import com.marttapps.swingrouter.Route;
import com.marttapps.swingrouter.Router;

public class App {

	private Router router;

	public App initLocale(String lang, String country) {
		Locale.setDefault(Locale.of(lang, country));
		return this;
	}

	public App initTheme(String themePath) {
		try {
			UIManager.setLookAndFeel(new FlatMacDarkLaf());
			IntelliJTheme.setup(App.class.getResourceAsStream(themePath));
		} catch (UnsupportedLookAndFeelException e) {
			DialogUtil.showErrorDialog("視窗樣式設定失敗");
		}
		return this;
	}

	public App useRouter(Route defaultRoute) {
		this.router = new Router(defaultRoute);
		return this;
	}

	public void launch() {
		new AppFrame(router);
	}

}
