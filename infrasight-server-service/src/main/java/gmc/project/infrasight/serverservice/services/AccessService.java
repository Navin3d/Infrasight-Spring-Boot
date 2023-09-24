package gmc.project.infrasight.serverservice.services;

import java.rmi.AccessException;

import gmc.project.infrasight.serverservice.entities.ServerEntity;
import gmc.project.infrasight.serverservice.entities.UserEntity;

public interface AccessService {
	public ServerEntity findOneServer(String serverId);
	public UserEntity findOneUser(String userId);
	
	public void transferServerOwnership(String serverId, String currentOwner, String userId) throws AccessException;
	public void provideAccessToServer(String serverId, String ownerId, String userId) throws AccessException;
	public void revokeAccessToServer(String serverId, String ownerId, String userId) throws AccessException;
}
