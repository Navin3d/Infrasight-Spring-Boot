package gmc.project.infrasight.statscaptureservice.services.impl;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.ServiceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gmc.project.infrasight.statscaptureservice.entities.ServerEntity;
import gmc.project.infrasight.statscaptureservice.entities.embedded.DiscMountEntity;
import gmc.project.infrasight.statscaptureservice.entities.embedded.DiscStatsEntity;
import gmc.project.infrasight.statscaptureservice.entities.embedded.IOStatData;
import gmc.project.infrasight.statscaptureservice.entities.embedded.IOStatEntity;
import gmc.project.infrasight.statscaptureservice.entities.embedded.StatsEntity;
import gmc.project.infrasight.statscaptureservice.models.MailingModel;
import gmc.project.infrasight.statscaptureservice.services.ProphetServiceFeignClient;
import gmc.project.infrasight.statscaptureservice.services.ServerService;
import gmc.project.infrasight.statscaptureservice.services.StatsService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StatsServiceImpl implements StatsService {

	@Autowired
	private ServerService serverService;
	@Autowired
	private ProphetServiceFeignClient prophetService;

	@Override
	public void storeDiscAndIOStat(String host, List<String> discResponse, List<String> ioResponseLines) throws ServiceNotFoundException {
		ServerEntity server = serverService.findOne(host);
		if (!(discResponse == null && ioResponseLines == null)) {
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
			IOStatEntity ioStatEntity = new IOStatEntity();
			Set<IOStatData> ioStats = new HashSet<>();
			String[] ioLines = ioResponseLines.get(0).split("\n");
			for(int lineNo = 6; lineNo < ioLines.length; lineNo++) {
				String ioLine = ioLines[lineNo];
				log.error(ioLine);
				IOStatData ioStatsdata = new IOStatData(ioLine);
				ioStats.add(ioStatsdata);
			}
			ioStatEntity.setIoDatas(ioStats);
			server.getIoStats().add(ioStatEntity);
			server.setIsActive(true);
		} else {
			server.setIsActive(false);
		}
		serverService.save(server);
	}

	@Override
	public void storeCPUAndRAM(String host, List<String> cpuLine, List<String> ramLine, List<String> swapLine, List<String> loadLine)
			throws ServiceNotFoundException {
		ServerEntity server = serverService.findOne(host);
		StatsEntity stats = new StatsEntity();
		String serverUptime = "O mins";
		if (!(cpuLine == null && ramLine == null && swapLine == null && loadLine == null)) {
			String[] cpulines;
			if (cpuLine.size() == 2)
				cpulines = cpuLine.get(1).split("\n");
			else
				cpulines = cpuLine.get(0).split("\n");
			stats = new StatsEntity(cpulines[3], ramLine.get(0), swapLine.get(0), loadLine.get(0));
			try {
				Long totalRam = stats.getTotalRam();
				Long freeRam = stats.getAvailableRam();
				Long usedRam = totalRam - freeRam; 
				double ramusedPercentage = (usedRam.doubleValue() / totalRam.doubleValue()) * 100D;
				Double cpuUse = stats.getCpuPerformance();
				log.error("ramusedPercentage: {}", ramusedPercentage);
				log.error("{}) {}: {} - {}", server.getName(), server.getRamLimit(), ((double) (usedRam / totalRam)), cpuUse.toString());
				LocalDate today = LocalDate.now();
//				if(server.getRamLimit() < ramusedPercentage && (server.getLastRamNotificationSent() == null || server.getLastRamNotificationSent().isBefore(today))) {
				if(server.getRamLimit() < ramusedPercentage) {
					MailingModel mail = new MailingModel();
					mail.setTo(server.getServerAdmin().getCompanyEmail());
					mail.setSubject("Update on your server " + server.getName());
					mail.setBody("Your server " + server.getName() + " has over throttled with RAM utilization of " + ramusedPercentage + "% and corresponding CPU usage is " + cpuUse + "%");
					prophetService.sendMail(mail);
//					server.setLastRamNotificationSent(today);
				}
//				}
//				if(server.getCpuLimit() < cpuUse && (server.getLastCpuNotificationSent() == null || server.getLastCpuNotificationSent().isBefore(today))) {
				if(server.getCpuLimit() < cpuUse) {
					MailingModel mail = new MailingModel();
					mail.setTo(server.getServerAdmin().getCompanyEmail());
					mail.setSubject("Update on your server " + server.getName());
					mail.setBody("Your server " + server.getName() + " has over throttled with CPU utilization of " + cpuUse + "% and corresponding RAM usage is " + ramusedPercentage + "%");
					prophetService.sendMail(mail);
//					server.setLastCpuNotificationSent(today);
				}
//				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("Error Sending mail.");
			}
			serverUptime = loadLine.get(0).split(",")[0].trim();
			log.error("serverUptime: {}", serverUptime);
			log.error("Ram: {}", stats.getTotalRam());
		}
		server.getRamCPU().add(stats);
		server.setIsActive(stats.getIsActive());
		server.setServerUpTime(serverUptime);
		serverService.save(server);
	}

}
