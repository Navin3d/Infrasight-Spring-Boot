package gmc.project.infrasight.statscaptureservice.services.impl;

import java.util.List;

import javax.management.ServiceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gmc.project.infrasight.statscaptureservice.daos.ServerDao;
import gmc.project.infrasight.statscaptureservice.entities.ServerEntity;
import gmc.project.infrasight.statscaptureservice.services.ServerService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ServerServiceImpl implements ServerService {
	
	@Autowired
	private ServerDao serverDao;

	@Override
	public ServerEntity findOne(String uniqueId) throws ServiceNotFoundException {
		ServerEntity foundServer = null;
		
		if(uniqueId.contains(".") || uniqueId.equals("localhost"))
			foundServer = serverDao.findByHost(uniqueId).orElse(null);
		else
			foundServer = serverDao.findById(uniqueId).orElse(null);
		
		if(foundServer == null) {
			log.error("Server with id {] bnot found.", uniqueId);
			throw new ServiceNotFoundException();
		}
		return foundServer;
	}

	@Override
	public List<ServerEntity> findAll() {
		return serverDao.findAll();
	}

	@Override
	public void save(ServerEntity serverEntity) {
		serverDao.save(serverEntity);
	}

}
