package gmc.project.infrasight.statscaptureservice.services;

import java.util.List;

import javax.management.ServiceNotFoundException;

import gmc.project.infrasight.statscaptureservice.entities.ServerEntity;

public interface ServerService {
	
	public ServerEntity findOne(String uniqueId) throws ServiceNotFoundException ;
	
	public List<ServerEntity> findAll();
	
	public void save(ServerEntity serverEntity);

}
