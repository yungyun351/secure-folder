package com.marttapps.securefolder.config;

import com.marttapps.securefolder.ui.feature.DecryptFileRoutePanel;
import com.marttapps.securefolder.ui.feature.EncryptFileRoutePanel;
import com.marttapps.securefolder.ui.feature.PreferencesRoutePanel;
import com.marttapps.swingrouter.Route;
import com.marttapps.swingrouter.RoutePanel;

import lombok.Getter;

/**
 * 路由設定
 */
@Getter
public enum Routes implements Route {

	/** 設定功能 */
	PREFERENCES(PreferencesRoutePanel.class, "設定", "/img/settings.png"),
	/** 加密功能 */
	ENCRYPT_FILE(EncryptFileRoutePanel.class, "加密", "/img/lock.png"),
	/** 解密功能 */
	DECRYPT_FILE(DecryptFileRoutePanel.class, "解密", "/img/key.png"),
	//
	;

	/** 面板 */
	private final Class<? extends RoutePanel> panelClass;
	/** 名稱 */
	private final String name;
	/** 圖示路徑 */
	private final String iconPath;

	private Routes(Class<? extends RoutePanel> panelClass, String name, String iconPath) {
		this.panelClass = panelClass;
		this.name = name;
		this.iconPath = iconPath;
	}

}
