package com.marttapps.securefolder.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marttapps.securefolder.Main;
import com.marttapps.securefolder.model.bean.Preferences;
import com.marttapps.securefolder.service.PreferencesService;

public class PreferencesServiceImpl implements PreferencesService {

	/** 偏好設定放置目錄名稱 */
	private static final String PREFS_DIR_NAME = "config";
	/** 偏好設定檔案名稱 */
	private static final String PREFS_FILE_NAME = "preferences.json";

	@Override
	public Preferences load() throws URISyntaxException {
		Path appDir = Paths.get(new File(Main.class //
				.getProtectionDomain() //
				.getCodeSource() //
				.getLocation() //
				.toURI() //
		).getParent());

		Path configPath = appDir.resolve(PREFS_DIR_NAME).resolve(PREFS_FILE_NAME);
		if (Files.notExists(configPath)) {
			return createDefaultPrefs();
		}

		ObjectMapper mapper = new ObjectMapper();
		Preferences prefs;
		try {
			prefs = mapper.readValue(configPath.toFile(), Preferences.class);
		} catch (IOException e) {
			return createDefaultPrefs();
		}
		return prefs;
	}

	@Override
	public void save(Preferences prefs) throws URISyntaxException, IOException {
		Path appDir = Paths.get(new File(Main.class //
				.getProtectionDomain() //
				.getCodeSource() //
				.getLocation() //
				.toURI() //
		).getParent());

		Path configPath = appDir.resolve(PREFS_DIR_NAME).resolve(PREFS_FILE_NAME);
		try {
			Files.createDirectories(configPath.getParent());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper
//		.writerWithDefaultPrettyPrinter()
					.writeValue(configPath.toFile(), prefs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 建立預設偏好設定
	 * 
	 * @return 預設偏好設定
	 */
	private Preferences createDefaultPrefs() {
		Preferences prefs = new Preferences();
		prefs.setAutoOpenDir(true);
		return prefs;
	}

}
