package com.marttapps.securefolder;

import javax.swing.SwingUtilities;

import com.marttapps.securefolder.config.Routes;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new App() //
				.initLocale("zh", "TW") //
				.initTheme("/theme/Cobalt_2.theme.json") //
				.useRouter(Routes.ENCRYPT_FILE) //
				.launch());
	}
}
