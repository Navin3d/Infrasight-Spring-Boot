package gmc.project.infrasight.presentationservice.services;

import java.rmi.AccessException;
import java.util.List;

import gmc.project.infrasight.presentationservice.entities.ServerEntity;
import gmc.project.infrasight.presentationservice.entities.UserEntity;

public interface UserService {
	public UserEntity findOne(String uniqueId) throws AccessException;
	public List<UserEntity> findAll();
	
	public List<ServerEntity> getUserAccessServers(String userId, String from, String to) throws Exception;
}
