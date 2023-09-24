package gmc.project.infrasight.presentationservice.services;

import java.rmi.server.ServerNotActiveException;
import java.util.List;

import gmc.project.infrasight.presentationservice.entities.ServerEntity;

public interface ServerService {
	public ServerEntity findOne(String uniqueId) throws ServerNotActiveException;
	public List<ServerEntity> findAll(Integer pageNo, Integer pageSize);
	public ServerEntity save(ServerEntity server);
	
	public List<ServerEntity> findServersByDates(Integer pageNo, Integer pageSize, String from, String to);
	public ServerEntity findByDateTime(String serverId, String from, String to) throws ServerNotActiveException;
	public ServerEntity findByDateTime(String serverId, String to) throws ServerNotActiveException;
	
	public List<ServerEntity> filterServerByDates(List<ServerEntity> servers, String from, String to);
}
