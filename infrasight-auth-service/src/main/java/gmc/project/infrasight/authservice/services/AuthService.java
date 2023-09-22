package gmc.project.infrasight.authservice.services;

import java.io.IOException;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import gmc.project.infrasight.authservice.entities.UserEntity;
import gmc.project.infrasight.authservice.models.UserModel;

public interface AuthService extends UserDetailsService {
	public UserEntity findOneUser(String uniqueId);
	public UserModel createUser(UserModel userModel, MultipartFile profilePic) throws IOException;
	public UserModel createUser(UserModel userModel) throws IOException;
}
