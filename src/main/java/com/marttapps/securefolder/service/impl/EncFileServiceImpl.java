package com.marttapps.securefolder.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.crypto.BadPaddingException;
import javax.crypto.SecretKey;

import com.marttapps.securefolder.codec.EncFileCodec;
import com.marttapps.securefolder.model.bean.EncMetadata;
import com.marttapps.securefolder.model.listener.EncFileProgressListener;
import com.marttapps.securefolder.service.CryptoService;
import com.marttapps.securefolder.service.EncFileService;

public class EncFileServiceImpl implements EncFileService {

	private static final String ENCRYPTED_FILE_EXTENSION = ".enc";

	@Override
	public void encryptFolder(Path rootPath, char[] pwd, EncFileProgressListener listener) throws IOException {
		List<Path> filesToProcess;
		try (Stream<Path> paths = Files.walk(rootPath)) {
			filesToProcess = paths.filter(Files::isRegularFile) //
					.filter(Predicate.not(this::isEncryptedFile)) //
					.toList();
		}

		listener.onStart(filesToProcess.size());

		int index = 0;
		for (Path file : filesToProcess) {
			if (listener.isCancelled())
				break;

			index++;
			listener.onFileStart(index, file);

			boolean succ = false;
			Exception e = null;
			try {
				encryptFile(file, file.getParent(), pwd);
				succ = true;
			} catch (IOException e1) {
				e = e1;
			}

			listener.onFileDone(index, file, succ, e);

			if (succ)
				Files.deleteIfExists(file);
		}

		listener.onCompleted();
	}

	@Override
	public void decryptFolder(Path rootPath, char[] pwd, EncFileProgressListener listener) throws IOException {
		List<Path> filesToProcess;
		try (Stream<Path> paths = Files.walk(rootPath)) {
			filesToProcess = paths.filter(Files::isRegularFile) //
					.filter(this::isEncryptedFile) //
					.toList();
		}

		listener.onStart(filesToProcess.size());

		int index = 0;
		for (Path file : filesToProcess) {
			if (listener.isCancelled())
				break;

			listener.onFileStart(index++, file);

			boolean succ = false;
			Exception e = null;
			try {
				decryptFile(file, file.getParent(), pwd);
				succ = true;
			} catch (IOException | BadPaddingException e1) {
				e = e1;
			}

			listener.onFileDone(index, file, succ, e);

			if (succ)
				Files.deleteIfExists(file);
		}

		listener.onCompleted();
	}

	private void encryptFile(Path plainFile, Path outputDir, char[] pwd) throws IOException {
		byte[] plain = Files.readAllBytes(plainFile);
		byte[] salt = CryptoService.INSTANCE.generateSalt();
		SecretKey key = CryptoService.INSTANCE.deriveKey(pwd, salt);
		byte[] encrypted = CryptoService.INSTANCE.encryptBytes(plain, key).orElse(null);
		if (encrypted == null)
			return;

		String fileName = plainFile.getFileName().toString();

		String id = UUID.randomUUID().toString().replace("-", "");
		Path encryptedFile = outputDir.resolve(id + ENCRYPTED_FILE_EXTENSION);
		new EncFileCodec().write(encryptedFile, fileName, salt, encrypted);
	}

	private void decryptFile(Path encryptedFile, Path outputDir, char[] pwd) throws IOException, BadPaddingException {
		EncMetadata meta = new EncFileCodec().read(encryptedFile);
		SecretKey key = CryptoService.INSTANCE.deriveKey(pwd, meta.getSalt());
		byte[] plain = CryptoService.INSTANCE.decryptBytes(meta.getEncryptedData(), key).orElse(null);
		if (plain == null)
			return;

		Path plainFile = outputDir.resolve(meta.getFileName());
		Files.write(plainFile, plain);
	}

	/**
	 * 是否為加密檔案
	 * 
	 * @param path
	 * @return
	 */
	private boolean isEncryptedFile(Path path) {
		return path.getFileName().toString().endsWith(ENCRYPTED_FILE_EXTENSION);
	}

}
