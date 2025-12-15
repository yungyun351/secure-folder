package com.marttapps.securefolder;

import java.util.Locale;

import javax.swing.SwingUtilities;

import com.marttapps.securefolder.config.Routes;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new App() //
				.initLocale(Locale.TRADITIONAL_CHINESE) //
				.initTheme("/theme/Cobalt_2.theme.json") //
				.useRouter(Routes.PREFERENCES) //
				.launch());
	}
}
