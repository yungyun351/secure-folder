package com.marttapps.securefolder.codec;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import com.marttapps.securefolder.model.bean.EncMetadata;

public class EncFileCodec {

	private static final byte[] MAGIC = { 'S', 'F', 'E', '1' };
	private static final int VERSION = 1;

	public EncMetadata read(Path encryptedFile) throws IOException {
		byte[] all = Files.readAllBytes(encryptedFile);
		int pos = 0;

		for (byte b : MAGIC) {
			if (all[pos++] != b) {
				throw new IOException("Invalid encrypted file format");
			}
		}

		int version = all[pos++] & 0xFF;
		if (version != VERSION) {
			throw new IOException("Unsupported version: " + version);
		}

		int nameLen = readUint16(all, pos);
		pos += 2;
		int saltLen = readUint16(all, pos);
		pos += 2;

		if (pos + nameLen + saltLen > all.length) {
			throw new IOException("Invalid metadata length");
		}

		String name = new String(all, pos, nameLen, StandardCharsets.UTF_8);
		pos += nameLen;

		byte[] salt = Arrays.copyOfRange(all, pos, pos + saltLen);
		pos += saltLen;

		byte[] encryptedData = Arrays.copyOfRange(all, pos, all.length);

		EncMetadata metadata = new EncMetadata();
		metadata.setFileName(name);
		metadata.setSalt(salt);
		metadata.setEncryptedData(encryptedData);
		return metadata;
	}

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

	private int readUint16(byte[] all, int offset) {
		return ((all[offset] & 0xFF) << 8) | (all[offset + 1] & 0xFF);
	}

	private void writeUint16(OutputStream out, int val) throws IOException {
		out.write((val >> 8) & 0xFF);
		out.write(val & 0xFF);
	}

}
