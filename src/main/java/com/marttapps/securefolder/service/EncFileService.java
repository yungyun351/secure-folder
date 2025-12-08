package com.marttapps.securefolder.service;

import java.io.IOException;
import java.nio.file.Path;

import com.marttapps.securefolder.model.listener.EncFileProgressListener;
import com.marttapps.securefolder.service.impl.EncFileServiceImpl;

public interface EncFileService {

	final EncFileService INSTANCE = new EncFileServiceImpl();

	/**
	 * 加密資料夾內的檔案
	 * 
	 * @param root 資料夾
	 * @param pwd  密碼
	 * @throws IOException
	 */
	public void encryptFolder(Path root, char[] pwd, EncFileProgressListener listener) throws IOException;

	/**
	 * 解密資料夾內的檔案
	 * 
	 * @param root 資料夾
	 * @param pwd  密碼
	 * @throws IOException
	 */
	public void decryptFolder(Path root, char[] pwd, EncFileProgressListener listener) throws IOException;

}
