package gmc.project.infrasight.authservice.services;

import org.springframework.security.core.userdetails.UserDetailsService;

import gmc.project.blockchain.legalchain.authservice.models.UserModel;

public interface AuthService extends UserDetailsService {
	public UserModel findOneUser(String uniqueId);
	public void createUser(UserModel userModel);
}
