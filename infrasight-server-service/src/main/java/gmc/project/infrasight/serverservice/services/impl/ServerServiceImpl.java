package gmc.project.infrasight.serverservice.services.impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gmc.project.infrasight.serverservice.daos.ServerDao;
import gmc.project.infrasight.serverservice.entities.ServerEntity;
import gmc.project.infrasight.serverservice.entities.UserEntity;
import gmc.project.infrasight.serverservice.models.ServerModel;
import gmc.project.infrasight.serverservice.services.AccessService;
import gmc.project.infrasight.serverservice.services.ServerService;
import gmc.project.infrasight.serverservice.utils.EncryptionUtil;

@Service
public class ServerServiceImpl implements ServerService {
	
	@Autowired
	private AccessService accessService;
	@Autowired
	private ServerDao serverDao;
	@Autowired
	private EncryptionUtil encryptUtil;
	
	private ModelMapper modelMapper = new ModelMapper();

	@Override
	public void addToMonitorList(ServerModel createServerModel) throws Exception {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		ServerEntity server = modelMapper.map(createServerModel, ServerEntity.class);
		server.setPassword(encryptUtil.encrypt(server.getPassword()));
		UserEntity serverAdmin = accessService.findOneUser(createServerModel.getServerOwnerId());
		server.setServerAdmin(serverAdmin);
		ServerEntity addedServer = serverDao.save(server);
		serverAdmin.getAdminOfServers().add(addedServer);
		accessService.saveUser(serverAdmin);
	}

	@Override
	public void updateServerDetails(ServerModel updatdServer) throws Exception {
		ServerEntity server = accessService.findOneServer(updatdServer.getId());
		updatdServer.setPassword(encryptUtil.encrypt((updatdServer.getPassword())));
		modelMapper.map(updatdServer, server);
		serverDao.save(server);
	}

	@Override
	public ServerModel getServer(String id, String userId) throws Exception {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		ServerEntity server = accessService.findOneServer(id);
		UserEntity foundUser = accessService.findOneUser(userId);
		if(server.getServerAdmin().equals(foundUser)) {
			ServerModel returnValue = modelMapper.map(server, ServerModel.class);
			returnValue.setPassword(encryptUtil.decrypt(returnValue.getPassword()));
			return returnValue;
		} else
			return new ServerModel();
		
	}

}
