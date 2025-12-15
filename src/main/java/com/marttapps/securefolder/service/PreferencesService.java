package com.marttapps.securefolder.service;

import java.io.IOException;
import java.net.URISyntaxException;

import com.marttapps.securefolder.model.bean.Preferences;
import com.marttapps.securefolder.service.impl.PreferencesServiceImpl;

/**
 * 偏好設定服務
 */
public interface PreferencesService {

	final PreferencesService INSTANCE = new PreferencesServiceImpl();

	/**
	 * 讀取偏好設定
	 * 
	 * @return 偏好設定檔
	 * @throws URISyntaxException 無法讀取偏好設定檔
	 */
	Preferences load() throws URISyntaxException;

	/**
	 * 寫入偏好設定檔
	 * 
	 * @param prefs 偏好設定檔
	 * @throws URISyntaxException 無法讀取偏好設定檔
	 * @throws IOException        無法寫入偏好設定檔
	 */
	void save(Preferences prefs) throws URISyntaxException, IOException;

}
