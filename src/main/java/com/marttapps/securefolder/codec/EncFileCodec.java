package com.marttapps.securefolder.codec;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import com.marttapps.securefolder.model.bean.EncFile;

/**
 * enc檔案編解碼器
 */
public class EncFileCodec {

	/** 特徵碼 Secure File Encryption v1 */
	private static final byte[] MAGIC = { 'S', 'F', 'E', '1' };

	/** 版本號 */
	private static final int VERSION = 1;

	/**
	 * 解析加密檔案
	 *
	 * @param encryptedFile 加密檔案路徑
	 * @return 解析後的加密檔案
	 * @throws IOException 格式錯誤或讀檔失敗
	 */
	public EncFile read(Path encryptedFile) throws IOException {
		byte[] all = Files.readAllBytes(encryptedFile);
		int pos = 0;

		// 4碼magic
		byte[] magic = Arrays.copyOfRange(all, pos, pos + MAGIC.length);
		if (!Arrays.equals(magic, MAGIC)) {
			throw new IOException("Invalid encrypted file format");
		}
		pos += MAGIC.length;

		// 1碼版號
		int version = all[pos++] & 0xFF;
		if (version != VERSION) {
			throw new IOException("Unsupported version: " + version);
		}

		// 2碼檔名長度
		int nameLen = readUint16(all, pos);
		pos += 2;

		// 2碼鹽長度
		int saltLen = readUint16(all, pos);
		pos += 2;

		// 檔名
		String name = new String(all, pos, nameLen, StandardCharsets.UTF_8);
		pos += nameLen;

		// 加密使用的鹽
		byte[] salt = Arrays.copyOfRange(all, pos, pos + saltLen);
		pos += saltLen;

		// 加密資料主體
		byte[] encryptedData = Arrays.copyOfRange(all, pos, all.length);

		EncFile metadata = new EncFile();
		metadata.setMagic(magic);
		metadata.setVersion(version);
		metadata.setFileName(name);
		metadata.setSalt(salt);
		metadata.setEncryptedData(encryptedData);
		return metadata;
	}

	/**
	 * 寫入加密檔案
	 *
	 * @param output        輸出路徑
	 * @param fileName      原始檔名
	 * @param salt          加密使用的鹽
	 * @param encryptedData 加密資料主體
	 * @throws IOException 寫入錯誤
	 */
	public void write(Path output, String fileName, byte[] salt, byte[] encryptedData) throws IOException {
		byte[] fileNameBytes = fileName.getBytes(StandardCharsets.UTF_8);
		try (OutputStream out = Files.newOutputStream(output)) {
			out.write(MAGIC);
			out.write(VERSION);
			writeUint16(out, fileNameBytes.length);
			writeUint16(out, salt.length);
			out.write(fileNameBytes);
			out.write(salt);
			out.write(encryptedData);
		}
	}

	/**
	 * 讀取uint16
	 * 
	 * @param all    整個byte陣列
	 * @param offset 起始位置
	 * @return uint16 (0~65535)
	 */
	private int readUint16(byte[] all, int offset) {
		return ((all[offset] & 0xFF) << 8) | (all[offset + 1] & 0xFF);
	}

	/**
	 * 寫入uint16
	 * 
	 * @param out 輸出資料流
	 * @param val 要寫入的值(0~65535)
	 * @throws IOException 寫入失敗
	 */
	private void writeUint16(OutputStream out, int val) throws IOException {
		out.write((val >> 8) & 0xFF);
		out.write(val & 0xFF);
	}

}
