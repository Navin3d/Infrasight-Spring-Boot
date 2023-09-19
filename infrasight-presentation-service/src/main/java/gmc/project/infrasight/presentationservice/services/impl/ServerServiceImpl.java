package gmc.project.infrasight.presentationservice.services.impl;

import java.rmi.server.ServerNotActiveException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import gmc.project.infrasight.presentationservice.daos.ServerDao;
import gmc.project.infrasight.presentationservice.entities.ServerEntity;
import gmc.project.infrasight.presentationservice.services.ServerService;

@Service
public class ServerServiceImpl implements ServerService {
	
	@Autowired
	private ServerDao serverDao;

	@Override
	public ServerEntity findOne(String uniqueId) throws ServerNotActiveException {
		ServerEntity foundServer = serverDao.findById(uniqueId).orElse(null);
		if(foundServer == null)
			throw new ServerNotActiveException();
		return foundServer;
	}

	@Override
	public List<ServerEntity> findAll(Integer pageNo, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<ServerEntity> foundServers = serverDao.findAll(pageable);		
		return foundServers.getContent();
	}

	@Override
	public ServerEntity save(ServerEntity server) {
		ServerEntity saved = serverDao.save(server);
		return saved;
	}

	@Override
	public ServerEntity findByDateTime(String serverId, String from, String to) throws ServerNotActiveException {
		LocalDateTime fromDateTime = LocalDateTime.parse(from);
		LocalDateTime toDateTime = LocalDateTime.parse(to);
		LocalDate fromDate = LocalDate.parse(from);
		LocalDate toDate = LocalDate.parse(to);
		ServerEntity foundServer = findOne(serverId);
		foundServer.getRamCPU().stream().filter(ramStats -> (ramStats.getCapturedAt().isBefore(toDateTime) && ramStats.getCapturedAt().isAfter(fromDateTime)));
		foundServer.getDiscStats().stream().filter(discStats -> (discStats.getCapturedAt().isBefore(toDate) && discStats.getCapturedAt().isAfter(fromDate)));
		return foundServer;
	}

	@Override
	public ServerEntity findByDateTime(String serverId, String to) throws ServerNotActiveException {
		LocalDateTime toDateTime = LocalDateTime.parse(to);
		LocalDate toDate = LocalDate.parse(to);
		ServerEntity foundServer = findOne(serverId);
		foundServer.getRamCPU().stream().filter(ramStats -> (ramStats.getCapturedAt().isBefore(toDateTime)));
		foundServer.getDiscStats().stream().filter(discStats -> (discStats.getCapturedAt().isBefore(toDate)));
		return foundServer;
	}

}
