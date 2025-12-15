package com.marttapps.securefolder.model.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 偏好設定
 */
@Getter
@Setter
public class Preferences {
	/** 是否要完成後開啟目錄 */
    private boolean autoOpenDir;
	/** 是否要記錄前一次目錄 */
	private boolean rememberLastFolder;
	/** 前一次目錄 */
	private String lastFolder;

}
