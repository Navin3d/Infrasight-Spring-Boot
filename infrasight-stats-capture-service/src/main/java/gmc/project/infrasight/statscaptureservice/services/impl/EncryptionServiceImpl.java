package gmc.project.infrasight.statscaptureservice.services.impl;

import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gmc.project.infrasight.statscaptureservice.config.EncryptionConfig;
import gmc.project.infrasight.statscaptureservice.services.EncryptionService;

@Service
public class EncryptionServiceImpl implements EncryptionService {
	
	@Autowired
	private EncryptionConfig encryConfig;

	public String encrypt(String data) throws Exception {
		KeySpec keySpec = new PBEKeySpec(encryConfig.getSecretKey().toCharArray(), encryConfig.getSecretKey().getBytes(), 65536, 256);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		SecretKey secretKey = factory.generateSecret(keySpec);
		SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

		Cipher cipher = Cipher.getInstance(encryConfig.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

		byte[] encryptedData = cipher.doFinal(data.getBytes());
		return Base64.getEncoder().encodeToString(encryptedData);
	}

	public String decrypt(String encryptedData) throws Exception {
		KeySpec keySpec = new PBEKeySpec(encryConfig.getSecretKey().toCharArray(), encryConfig.getSecretKey().getBytes(), 65536, 256);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		SecretKey secretKey = factory.generateSecret(keySpec);
		SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

		Cipher cipher = Cipher.getInstance(encryConfig.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

		byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
		return new String(decryptedData);
	}
	
}
