package gmc.project.infrasight.statscaptureservice.services.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.ServiceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gmc.project.infrasight.statscaptureservice.entities.ServerEntity;
import gmc.project.infrasight.statscaptureservice.entities.embedded.DiscMountEntity;
import gmc.project.infrasight.statscaptureservice.entities.embedded.DiscStatsEntity;
import gmc.project.infrasight.statscaptureservice.entities.embedded.StatsEntity;
import gmc.project.infrasight.statscaptureservice.services.ServerService;
import gmc.project.infrasight.statscaptureservice.services.StatsService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StatsServiceImpl implements StatsService {

	@Autowired
	private ServerService serverService;

	@Override
	public void storeDiscStat(String host, List<String> discResponse) throws ServiceNotFoundException {
		ServerEntity server = serverService.findOne(host);
		if (!(discResponse == null)) {
			DiscStatsEntity discStats = new DiscStatsEntity();
			Set<DiscMountEntity> discMounts = new HashSet<>();
			for (String discLines : discResponse) {
				String[] lines = discLines.split("\n");
				for (int i = 1; i < lines.length; i++) {
					String line = lines[i];
					DiscMountEntity discMount = new DiscMountEntity(line);
					discMounts.add(discMount);
				}
			}
			discStats.setDiscMounts(discMounts);
			server.getDiscStats().add(discStats);
			server.setIsActive(true);
		} else {
			server.setIsActive(false);
		}
		serverService.save(server);
	}

	@Override
	public void storeCPUAndRAM(String host, List<String> cpuLine, List<String> ramLine)
			throws ServiceNotFoundException {
		ServerEntity server = serverService.findOne(host);
		StatsEntity stats = new StatsEntity();
		if (!(cpuLine == null && ramLine == null)) {
			String[] cpulines;
			if (cpuLine.size() == 2)
				cpulines = cpuLine.get(1).split("\n");
			else
				cpulines = cpuLine.get(0).split("\n");
			stats = new StatsEntity(cpulines[3], ramLine.get(0));
			log.error("Ram: {}", stats.getTotalRam());
		}
		server.getRamCPU().add(stats);
		server.setIsActive(stats.getIsActive());
		serverService.save(server);
	}

}
