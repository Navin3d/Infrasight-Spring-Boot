package gmc.project.infrasight.serverservice.services.impl;

import java.rmi.AccessException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gmc.project.infrasight.serverservice.daos.ServerDao;
import gmc.project.infrasight.serverservice.daos.UserDao;
import gmc.project.infrasight.serverservice.entities.ServerEntity;
import gmc.project.infrasight.serverservice.entities.UserEntity;
import gmc.project.infrasight.serverservice.services.AccessService;

@Service
public class AccessServiceImpl implements AccessService {

	@Autowired
	private ServerDao serverdao;
	@Autowired
	private UserDao userDao;
	
	@Override
	public ServerEntity findOneServer(String serverId) {
		ServerEntity foundserver = serverdao.findById(serverId).orElse(null);
		if(foundserver == null)
			throw new RuntimeException(serverId);
		return foundserver;
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
			foundUser = userDao.findByUsername(uniqueId).orElse(null);
		}
		if (foundUser == null) {
			throw new RuntimeException(uniqueId);
		}
		return foundUser;
	}

	@Override
	public void transferServerOwnership(String serverId, String ownerId, String userId) throws AccessException {
		UserEntity foundUser = findOneUser(userId);
		UserEntity serverAdmin = findOneUser(ownerId);
		ServerEntity foundServer = findOneServer(serverId);
		if(!foundServer.getServerAdmin().equals(serverAdmin))
			throw new AccessException(serverId);
		foundServer.setServerAdmin(foundUser);
		serverdao.save(foundServer);
		foundUser.getAdminOfServers().add(foundServer);
		userDao.save(foundUser);
	}

	@Override
	public void provideAccessToServer(String serverId, String ownerId, String userId) throws AccessException {
		UserEntity foundUser = findOneUser(userId);
		UserEntity serverAdmin = findOneUser(ownerId);
		ServerEntity foundServer = findOneServer(serverId);
		if(!foundServer.getServerAdmin().equals(serverAdmin))
			throw new AccessException(serverId);
		foundServer.getServerUsers().add(foundUser);
		serverdao.save(foundServer);
		foundUser.getAccessibleServers().add(foundServer);
		userDao.save(foundUser);
	}

	@Override
	public void revokeAccessToServer(String serverId, String ownerId, String userId) throws AccessException {
		UserEntity foundUser = findOneUser(userId);
		UserEntity serverAdmin = findOneUser(ownerId);
		ServerEntity foundServer = findOneServer(serverId);
		if(!foundServer.getServerAdmin().equals(serverAdmin))
			throw new AccessException(serverId);
		foundServer.getServerUsers().remove(foundUser);
		serverdao.save(foundServer);
		foundUser.getAccessibleServers().remove(foundServer);
		userDao.save(foundUser);
	}

}
