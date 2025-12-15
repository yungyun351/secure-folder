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

	/** 路由器 */
	private Router router;

	/**
	 * 初始化語言環境
	 * 
	 * @param locale 語言環境
	 * @return 方法鏈
	 */
	public App initLocale(Locale locale) {
		Locale.setDefault(locale);
		return this;
	}

	/**
	 * 初始化主題樣式
	 * 
	 * @param themePath 主題設定路徑
	 * @return 方法鏈
	 */
	public App initTheme(String themePath) {
		try {
			UIManager.setLookAndFeel(new FlatMacDarkLaf());
			IntelliJTheme.setup(App.class.getResourceAsStream(themePath));
		} catch (UnsupportedLookAndFeelException e) {
			DialogUtil.showErrorDialog("視窗樣式設定失敗。");
		}
		return this;
	}

	/**
	 * 使用路由
	 * 
	 * @param defaultRoute 預設路由
	 * @return 方法鏈
	 */
	public App useRouter(Route defaultRoute) {
		this.router = new Router(defaultRoute);
		return this;
	}

	/**
	 * 啟動
	 */
	public void launch() {
		new AppFrame(router);
	}

}
