package com.marttapps.securefolder.service;

import java.io.IOException;
import java.nio.file.Path;

import com.marttapps.securefolder.model.listener.EncFileProgressListener;
import com.marttapps.securefolder.service.impl.EncFileServiceImpl;

/**
 * 加密檔案服務
 */
public interface EncFileService {

	final EncFileService INSTANCE = new EncFileServiceImpl();

	/**
	 * 加密資料夾內的檔案
	 * 
	 * @param root 資料夾
	 * @param pwd  密碼
	 * @throws IOException 讀檔失敗
	 */
	void encryptFolder(Path root, char[] pwd, EncFileProgressListener listener) throws IOException;

	/**
	 * 解密資料夾內的檔案
	 * 
	 * @param root 資料夾
	 * @param pwd  密碼
	 * @throws IOException 讀檔失敗
	 */
	void decryptFolder(Path root, char[] pwd, EncFileProgressListener listener) throws IOException;

	/**
	 * 是否為加密檔案
	 * 
	 * @param path 檔案路徑
	 * @return 是否為加密檔案
	 */
	boolean isEncryptedFile(Path path);

	/**
	 * 是否不是加密檔案
	 * 
	 * @param path 檔案路徑
	 * @return 是否不是加密檔案
	 */
	boolean isNotEncryptedFile(Path path);

}
