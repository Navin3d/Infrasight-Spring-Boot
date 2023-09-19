package gmc.project.infrasight.authservice.services;

import gmc.project.infrasight.authservice.models.UserModel;

public interface UserService {
	
	public UserModel getProfile(String userId);

}
