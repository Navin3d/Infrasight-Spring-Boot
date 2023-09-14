package gmc.project.infrasight.serverservice.services.impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gmc.project.infrasight.serverservice.daos.ServerDao;
import gmc.project.infrasight.serverservice.entities.ServerEntity;
import gmc.project.infrasight.serverservice.models.CreateServerModel;
import gmc.project.infrasight.serverservice.services.ServerService;
import gmc.project.infrasight.serverservice.utils.EncryptionUtil;

@Service
public class ServerServiceImpl implements ServerService {
	
	@Autowired
	private ServerDao serverDao;
	@Autowired
	private EncryptionUtil encryptUtil;
	
	private ModelMapper modelMapper = new ModelMapper();

	@Override
	public void addToMonitorList(CreateServerModel createServerModel) throws Exception {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		ServerEntity server = modelMapper.map(createServerModel, ServerEntity.class);
		server.setPassword(encryptUtil.encrypt(server.getPassword()));
		serverDao.save(server);
	}

}
