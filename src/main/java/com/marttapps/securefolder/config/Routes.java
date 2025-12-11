package com.marttapps.securefolder.config;

import com.marttapps.securefolder.ui.feature.DecryptFileRoutePanel;
import com.marttapps.securefolder.ui.feature.EncryptFileRoutePanel;
import com.marttapps.swingrouter.Route;
import com.marttapps.swingrouter.RoutePanel;

/**
 * 路由設定
 */
public enum Routes implements Route {

	/** 加密功能 */
	ENCRYPT_FILE(EncryptFileRoutePanel.class),
	/** 解密功能 */
	DECRYPT_FILE(DecryptFileRoutePanel.class),
	//
	;

	private final Class<? extends RoutePanel> panelClass;

	private Routes(Class<? extends RoutePanel> panelClass) {
		this.panelClass = panelClass;
	}

	public Class<? extends RoutePanel> getPanelClass() {
		return panelClass;
	}

}
