package gmc.project.infrasight.statscaptureservice.services;

public interface EncryptionService {
	public String encrypt(String rawData) throws Exception;
	public String decrypt(String encrypteData) throws Exception;
}
