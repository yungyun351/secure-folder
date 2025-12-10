package com.marttapps.securefolder.service.impl;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.marttapps.securefolder.service.CryptoService;

public class CryptoServiceImpl implements CryptoService {

	private static final String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";

	@Override
	public byte[] generateSalt() {
		byte[] salt = new byte[16];
		new SecureRandom().nextBytes(salt);
		return salt;
	}

	@Override
	public SecretKey deriveKey(char[] password, byte[] salt) {
		SecretKeyFactory factory;
		try {
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		} catch (NoSuchAlgorithmException e) {
			// NoSuchAlgorithmException: JVM不支援AES/GCM/NoPadding(不處理，Jdk8後內建)
			e.printStackTrace();
			return null;
		}

		PBEKeySpec spec = new PBEKeySpec(password, salt, 100_000, 256);
		SecretKey secretKey;
		try {
			secretKey = factory.generateSecret(spec);
		} catch (InvalidKeySpecException e) {
			// NoSuchAlgorithmException: spec裡面的參數不合法(不處理)
			e.printStackTrace();
			return null;
		}

		return new SecretKeySpec(secretKey.getEncoded(), "AES");
	}

	@Override
	public Optional<byte[]> encryptBytes(byte[] plain, SecretKey key) {
		byte[] iv = new byte[12];
		new SecureRandom().nextBytes(iv);

		Cipher cipher;
		try {
			cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// NoSuchAlgorithmException: JVM不支援AES/GCM/NoPadding(不處理，Jdk8後內建)
			// NoSuchPaddingException: 用了不存在的padding名稱(不處理，GCM模式本身不需要padding)
			e.printStackTrace();
			return Optional.empty();
		}

		try {
			cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, iv));
		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
			// InvalidKeyException: Key長度或格式錯誤(不處理)
			// InvalidAlgorithmParameterException: iv,tag長度不正確(不處理)
			e.printStackTrace();
			return Optional.empty();
		}

		byte[] cipherContent;
		try {
			cipherContent = cipher.doFinal(plain);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			// IllegalBlockSizeException: 傳入的明文長度不符合演算法要求(不處理，GCM模式可接受任意長度)
			// BadPaddingException: 解密失敗(不處理，此處為加密階段)
			e.printStackTrace();
			return Optional.empty();
		}

		ByteBuffer bb = ByteBuffer.allocate(iv.length + cipherContent.length);
		bb.put(iv);
		bb.put(cipherContent);
		return Optional.of(bb.array());
	}

	@Override
	public Optional<byte[]> decryptBytes(byte[] encrypted, SecretKey key) throws BadPaddingException {
		ByteBuffer bb = ByteBuffer.wrap(encrypted);

		byte[] iv = new byte[12];
		bb.get(iv);

		byte[] cipherContent = new byte[bb.remaining()];
		bb.get(cipherContent);

		Cipher cipher;
		try {
			cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// NoSuchAlgorithmException: JVM不支援AES/GCM/NoPadding(不處理，Jdk8後內建)
			// NoSuchPaddingException: 用了不存在的padding名稱(不處理，GCM模式本身不需要padding)
			e.printStackTrace();
			return Optional.empty();
		}

		try {
			cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, iv));
		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
			// InvalidKeyException: Key長度或格式錯誤(不處理)
			// InvalidAlgorithmParameterException: iv,tag長度不正確(不處理)
			e.printStackTrace();
			return Optional.empty();
		}

		byte[] plain;
		try {
			plain = cipher.doFinal(cipherContent);
		} catch (IllegalBlockSizeException e) {
			// IllegalBlockSizeException: 傳入的明文長度不符合演算法要求(不處理，GCM模式可接受任意長度)
			e.printStackTrace();
			return Optional.empty();
		}
		return Optional.of(plain);
	}

}
