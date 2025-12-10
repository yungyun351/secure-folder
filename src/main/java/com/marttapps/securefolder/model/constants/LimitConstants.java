package com.marttapps.securefolder.model.constants;

/**
 * 限制相關的常數
 */
public class LimitConstants {

	private LimitConstants() {
		throw new UnsupportedOperationException("Class should not be instantiated");
	}

	/** 檔案總大小達到此值時提示使用者 */
	public static final long SECURE_FOLDER_SIZE_WARNING = 1L * 1024 * 1024 * 1024;

	/** 檔案總大小達到此值時禁止操作 */
	public static final long SECURE_FOLDER_SIZE_LIMIT = 50L * 1024 * 1024 * 1024;

}
