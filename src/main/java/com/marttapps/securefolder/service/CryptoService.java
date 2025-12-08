package com.marttapps.securefolder.service;

import java.io.IOException;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.SecretKey;

import com.marttapps.securefolder.service.impl.CryptoServiceImpl;

/**
 * 加解密服務
 */
public interface CryptoService {

	final CryptoService INSTANCE = new CryptoServiceImpl();

	/**
	 * 產生鹽
	 * 
	 * @return 鹽
	 */
	byte[] generateSalt();

	/**
	 * 派生金鑰
	 * 
	 * @param password 密碼
	 * @param salt     鹽
	 * @return 金鑰
	 */
	SecretKey deriveKey(char[] password, byte[] salt);


	/**
	 * 加密
	 * 
	 * @param plain
	 * @param key   金鑰
	 * @return
	 * @throws IOException
	 */
	Optional<byte[]> encryptBytes(byte[] plain, SecretKey key) throws IOException;

	/**
	 * 解密
	 * 
	 * @param cipher
	 * @param key    金鑰
	 * @return
	 * @throws IOException
	 * @throws BadPaddingException
	 */
	Optional<byte[]> decryptBytes(byte[] cipher, SecretKey key) throws IOException, BadPaddingException;

	/**
	 * 解密
	 * 
	 * @param encryptedFile 加密檔案
	 * @param plainFile     普通檔案
	 * @param key           金鑰
	 * @throws IOException
	 * @throws BadPaddingException
	 */

}
