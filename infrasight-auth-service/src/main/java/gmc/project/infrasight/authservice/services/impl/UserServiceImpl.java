package gmc.project.infrasight.authservice.services.impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gmc.project.infrasight.authservice.entities.UserEntity;
import gmc.project.infrasight.authservice.models.UserModel;
import gmc.project.infrasight.authservice.services.AuthService;
import gmc.project.infrasight.authservice.services.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private AuthService authService;
	
	private ModelMapper modelMapper = new ModelMapper();

	@Override
	public UserModel getProfile(String userId) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity foundUser = authService.findOneUser(userId);
		UserModel returnValue = modelMapper.map(foundUser, UserModel.class);
		return returnValue;
	}

}
