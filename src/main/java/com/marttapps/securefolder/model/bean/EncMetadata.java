package com.marttapps.securefolder.model.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EncMetadata {
	private String fileName;
	private byte[] salt;
	private byte[] encryptedData;
}
