package gmc.project.infrasight.authservice.services.impl;

import java.io.IOException;
import java.util.ArrayList;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import gmc.project.infrasight.authservice.daos.UserDao;
import gmc.project.infrasight.authservice.entities.UserEntity;
import gmc.project.infrasight.authservice.models.UserModel;
import gmc.project.infrasight.authservice.services.AuthService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private ModelMapper modelMapper = new ModelMapper();

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = findOneUser(username);
		return new User(username, user.getPassword(), new ArrayList<>());
	}

	@Override
	public UserEntity findOneUser(String uniqueId) {
		UserEntity foundUser = null;
		if (uniqueId.contains("@")) {
			foundUser = userDao.findByCompanyEmail(uniqueId).orElse(null);
		} else {
			try {
				Integer employeeId = Integer.valueOf(uniqueId);
				foundUser = userDao.findByEmployeeId(employeeId).orElse(null);
			} catch (NumberFormatException e) {
				foundUser = userDao.findById(uniqueId).orElse(null);
			}
		}
		if (foundUser == null) {
			log.info("The entered unique id is username.");
			foundUser = userDao.findByUsername(uniqueId).orElse(null);
		}
		if (foundUser == null) {
			log.error("The user with id {} not found.", uniqueId);
			throw new UsernameNotFoundException(uniqueId);
		}
		return foundUser;
	}

	@Override
	public UserModel createUser(UserModel userModel, MultipartFile profilePic) throws IOException {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity detachedUser = modelMapper.map(userModel, UserEntity.class);
		detachedUser.setPassword(bCryptPasswordEncoder.encode(userModel.getPasswordPlain()));
		detachedUser.setProfilePic(new Binary(BsonBinarySubType.BINARY, profilePic.getBytes()));
		UserEntity createdUser = userDao.insert(detachedUser);
		userModel.setId(createdUser.getId());
		return userModel;
	}

	@Override
	public UserModel createUser(UserModel userModel) throws IOException {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity detachedUser = modelMapper.map(userModel, UserEntity.class);
		detachedUser.setPassword(bCryptPasswordEncoder.encode(userModel.getPasswordPlain()));
		UserEntity createdUser = userDao.insert(detachedUser);
		userModel.setId(createdUser.getId());
		return userModel;
	}

}
