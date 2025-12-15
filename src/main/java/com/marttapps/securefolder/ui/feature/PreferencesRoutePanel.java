package com.marttapps.securefolder.ui.feature;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.UIManager;

import com.marttapps.securefolder.ui.component.SwitchSettingItem;
import com.marttapps.securefolder.util.DialogUtil;
import com.marttapps.securefolder.model.bean.Preferences;
import com.marttapps.securefolder.model.constants.UiConstants;
import com.marttapps.securefolder.service.PreferencesService;
import com.marttapps.swingrouter.RoutePanel;
import com.marttapps.swingrouter.Router;

/**
 * 設定功能
 */
public class PreferencesRoutePanel extends RoutePanel {

	private static final long serialVersionUID = 1L;

	/** 完成後開啟目錄欄位 */
	private SwitchSettingItem autoOpenDirSwitch;
	/** 記錄前一次目錄欄位 */
	private SwitchSettingItem rememberLastFolderSwitch;

	public PreferencesRoutePanel(Router router) {
		super(router);
		render();
	}

	private void render() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		int errorSize = 18;
		int titleSize = UiConstants.STYLE_DEFAULT_FONT_SIZE + 2;
		int descSize = UiConstants.STYLE_DEFAULT_FONT_SIZE;
		int switchSize = 32;
		int hintSize = 12;
		Color borderColor = UIManager.getColor(UiConstants.LABEL_FOREGROUND);

		Box content = Box.createVerticalBox();

		Preferences prefs;
		try {
			prefs = PreferencesService.INSTANCE.load();
		} catch (URISyntaxException e) {
			JLabel error = new JLabel("執行目錄有誤，無法處理偏好設定。");
			error.setFont(new Font(UiConstants.STYLE_DEFAULT_FONT_NAME, Font.BOLD, errorSize));
			error.setForeground(Color.RED);
			content.add(error);
			add(content);
			return;
		}

		autoOpenDirSwitch = new SwitchSettingItem("完成後開啟目錄", "當功能執行完成之後，自動開啟目錄。", titleSize, descSize, switchSize,
				this::exec);
		autoOpenDirSwitch.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, borderColor));
		if (Desktop.isDesktopSupported()) {
			autoOpenDirSwitch.setSwitchSelected(prefs.isAutoOpenDir());
		} else {
			autoOpenDirSwitch.setSwitchSelected(false);
			autoOpenDirSwitch.setSwitchEnabled(false);
			JLabel hint = new JLabel("系統不支援");
			hint.setFont(new Font(UiConstants.STYLE_DEFAULT_FONT_NAME, Font.BOLD, hintSize));
			hint.setForeground(Color.RED);
			autoOpenDirSwitch.addTextButtomComponent(hint);
		}
		content.add(autoOpenDirSwitch);

		rememberLastFolderSwitch = new SwitchSettingItem("記錄前一次目錄", "啟用後將會自動帶入前一次執行時所輸入的目錄。", titleSize, descSize,
				switchSize, this::exec);
		rememberLastFolderSwitch.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, borderColor));
		rememberLastFolderSwitch.setSwitchSelected(prefs.isRememberLastFolder());
		content.add(rememberLastFolderSwitch);

		add(content);
	}

	/**
	 * 執行偏好設定
	 * 
	 * @param e
	 */
	private void exec(ActionEvent e) {
		Preferences prefs;
		try {
			prefs = PreferencesService.INSTANCE.load();
		} catch (URISyntaxException e1) {
			DialogUtil.showWarnDialog("執行目錄有誤，無法寫入偏好設定檔。");
			return;
		}

		prefs.setAutoOpenDir(autoOpenDirSwitch.isSwitchSelected());

		boolean rememberLastFolder = rememberLastFolderSwitch.isSwitchSelected();
		prefs.setRememberLastFolder(rememberLastFolder);
		if (!rememberLastFolder) {
			prefs.setLastFolder(null);
		}
		
		try {
			PreferencesService.INSTANCE.save(prefs);
		} catch (URISyntaxException | IOException e2) {
			DialogUtil.showWarnDialog("執行目錄有誤，無法寫入偏好設定檔。");
		}
	}

}
