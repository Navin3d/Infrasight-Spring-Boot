package gmc.project.infrasight.presentationservice.services.impl;

import java.rmi.server.ServerNotActiveException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import gmc.project.infrasight.presentationservice.daos.ServerDao;
import gmc.project.infrasight.presentationservice.entities.ServerEntity;
import gmc.project.infrasight.presentationservice.entities.embedded.DiscStatsEntity;
import gmc.project.infrasight.presentationservice.entities.embedded.StatsEntity;
import gmc.project.infrasight.presentationservice.services.ServerService;
import gmc.project.infrasight.presentationservice.utils.DateTimeUtil;

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
		LocalDateTime fromDateTime = DateTimeUtil.parseDateTime(from);
		LocalDateTime toDateTime = DateTimeUtil.parseDateTime(to);
		LocalDate fromDate = fromDateTime.toLocalDate();
		LocalDate toDate = toDateTime.toLocalDate();
		ServerEntity foundServer = findOne(serverId);
		List<StatsEntity> ramCpu = foundServer.getRamCPU().stream().filter(ramStats -> (ramStats.getCapturedAt().isBefore(toDateTime) && ramStats.getCapturedAt().isAfter(fromDateTime))).toList();
		List<DiscStatsEntity> discs = foundServer.getDiscStats().stream().filter(discStats -> (discStats.getCapturedAt().isBefore(toDate) && discStats.getCapturedAt().isAfter(fromDate))).toList();
		foundServer.getDiscStats().clear();
		foundServer.getRamCPU().clear();
		foundServer.getDiscStats().addAll(discs);
		foundServer.getRamCPU().addAll(ramCpu);
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

	@Override
	public List<ServerEntity> findServersByDates(Integer pageNo, Integer pageSize, String from, String to) {
		List<ServerEntity> foundServers = findAll(pageNo, pageSize);
		return filterServerByDates(foundServers, from, to);
	}

	@Override
	public List<ServerEntity> filterServerByDates(List<ServerEntity> servers, String from, String to) {
		LocalDate fromDate = DateTimeUtil.parseDate(from);
		LocalDate toDate = DateTimeUtil.parseDate(to);
		List<ServerEntity> returnValue = new ArrayList<>();
		for(ServerEntity server: servers) {
			List<StatsEntity> rams = server.getRamCPU().stream().filter(ram -> {
				LocalDate capturedDate = ram.getCapturedAt().toLocalDate();
				if(capturedDate.isBefore(toDate) && capturedDate.isAfter(fromDate))
					return true;
				else
					return false;
			}).toList();
			server.getRamCPU().clear();
			server.getRamCPU().addAll(rams);
			
			List<DiscStatsEntity> discs = server.getDiscStats().stream().filter(disc -> {
				LocalDate capturedDate = disc.getCapturedAt();
				if(capturedDate.isBefore(toDate) && capturedDate.isAfter(fromDate))
					return true;
				else
					return false;
			}).toList();
			server.getDiscStats().clear();
			server.getDiscStats().addAll(discs);
			
			returnValue.add(server);
		}
		return returnValue;
	}

}
