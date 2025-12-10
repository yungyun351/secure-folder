package com.marttapps.securefolder.config;

import com.marttapps.securefolder.ui.feature.DecryptFileRoutePanel;
import com.marttapps.securefolder.ui.feature.EncryptFileRoutePanel;
import com.marttapps.swingrouter.Route;
import com.marttapps.swingrouter.RoutePanel;

/**
 * 路由
 */
public enum Routes implements Route {
	DECRYPT_FILE(DecryptFileRoutePanel.class), //
	ENCRYPT_FILE(EncryptFileRoutePanel.class), //
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
