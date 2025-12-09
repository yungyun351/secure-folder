package com.marttapps.securefolder.model.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 加密檔案
 */
@Getter
@Setter
public class EncFile {
	/** 特徵碼 */
	private byte[] magic;
	/** 版號 */
	private int version;
	/** 檔名 */
	private String fileName;
	/** 加密使用的鹽 */
	private byte[] salt;
	/** 加密資料主體 */
	private byte[] encryptedData;
}
