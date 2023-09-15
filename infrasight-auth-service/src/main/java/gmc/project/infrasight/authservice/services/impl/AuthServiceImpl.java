package gmc.project.infrasight.authservice.services.impl;

import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import gmc.project.infrasight.authservice.daos.UserDao;
import gmc.project.infrasight.authservice.entities.UserEntity;
import gmc.project.infrasight.authservice.models.UserModel;
import gmc.project.infrasight.authservice.services.AuthService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
	
	@Value("${hyperledger.wallet.admin.username}")
	private String adminUser;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private ModelMapper modelMapper = new ModelMapper();

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserModel user = findOneUser(username);
		return new User(username, user.getEncryptedPassword(), new ArrayList<>());
	}

	@Override
	public UserModel findOneUser(String uniqueId) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity foundUser = null;
		if(uniqueId.contains("@")) {
			log.info("The entered unique id is Email.");
			foundUser = userDao.findByEmail(uniqueId).orElse(null);
		} else {
			try {
				Long.valueOf(uniqueId);
				if(uniqueId.length() == 12) {
					log.info("The entered unique id is Aadhar Id.");
					foundUser = userDao.findByAadharNumber(uniqueId).orElse(null);
				}
				if(uniqueId.length() == 10) {
					log.info("The entered unique id is Mobile Number.");
					foundUser = userDao.findByMobileNumber(uniqueId).orElse(null);
				}
			} catch(NumberFormatException e) {
				log.info("The entered unique id is Id.");
				foundUser = userDao.findById(uniqueId).orElse(null);
			}
		}
		if(foundUser == null) {
			log.info("The entered unique id is username.");
			foundUser = userDao.findByUsername(uniqueId).orElse(null);
		}
		if(foundUser == null) {
			log.error("The user with id {} not found.", uniqueId);
			throw new UsernameNotFoundException(uniqueId);
		}
		UserModel returnValue = modelMapper.map(foundUser, UserModel.class);
		return returnValue;
	}

	@Override
	public void createUser(UserModel userModel) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity detachedUser = modelMapper.map(userModel, UserEntity.class);
		detachedUser.setEncryptedPassword(bCryptPasswordEncoder.encode(userModel.getPassword()));
		
		try {
			userDao.insert(detachedUser);
			HyperledgerSignUpModel signUpModel = new HyperledgerSignUpModel(adminUser, userModel.getUsername());
			if(userModel.getIsLegalUser()) {
				signUpModel.setOrgMspId("Org1MSP");
				signUpModel.setAffiliation("org1.department1");
				blockchainService.createLegalWalletUser(signUpModel);
			} else {
				signUpModel.setOrgMspId("Org2MSP");
				signUpModel.setAffiliation("org2.department2");
				blockchainService.createCivilWalletUser(signUpModel);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

}
