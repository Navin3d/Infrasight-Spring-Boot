package gmc.project.infrasight.presentationservice.services.impl;

import java.rmi.AccessException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gmc.project.infrasight.presentationservice.daos.UserDao;
import gmc.project.infrasight.presentationservice.entities.ServerEntity;
import gmc.project.infrasight.presentationservice.entities.UserEntity;
import gmc.project.infrasight.presentationservice.services.ServerService;
import gmc.project.infrasight.presentationservice.services.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private ServerService serverService;
	@Autowired
	private UserDao userDao;

	@Override
	public UserEntity findOne(String uniqueId) throws AccessException {
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
			foundUser = userDao.findByUsername(uniqueId).orElse(null);
		}
		if (foundUser == null) {
			throw new AccessException(uniqueId);
		}
		return foundUser;
	}

	@Override
	public List<ServerEntity> getUserAccessServers(String userId, String from, String to) throws Exception {
		UserEntity foundUser = findOne(userId);
		List<ServerEntity> servers = new ArrayList<>();
		servers.addAll(foundUser.getAdminOfServers());
		servers.addAll(foundUser.getAccessibleServers());
		return serverService.filterServerByDates(servers, from, to);
	}

	@Override
	public List<UserEntity> findAll() {
		return userDao.findAll();
	}

}
