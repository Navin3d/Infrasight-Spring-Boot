package gmc.project.infrasight.serverservice.utils;

import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gmc.project.infrasight.serverservice.config.EncryptionConfig;

@Component
public class EncryptionUtil {
	
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