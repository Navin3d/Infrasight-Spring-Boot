package gmc.project.infrasight.statscaptureservice.services.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.ServiceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gmc.project.infrasight.statscaptureservice.daos.ProjectDao;
import gmc.project.infrasight.statscaptureservice.entities.ProjectEntity;
import gmc.project.infrasight.statscaptureservice.entities.ServerEntity;
import gmc.project.infrasight.statscaptureservice.entities.embedded.DiscMountEntity;
import gmc.project.infrasight.statscaptureservice.entities.embedded.DiscStatsEntity;
import gmc.project.infrasight.statscaptureservice.entities.embedded.IOStatData;
import gmc.project.infrasight.statscaptureservice.entities.embedded.IOStatEntity;
import gmc.project.infrasight.statscaptureservice.entities.embedded.StatsEntity;
import gmc.project.infrasight.statscaptureservice.models.MailingModel;
import gmc.project.infrasight.statscaptureservice.services.MLServiceFeignClient;
import gmc.project.infrasight.statscaptureservice.services.ProphetServiceFeignClient;
import gmc.project.infrasight.statscaptureservice.services.ServerService;
import gmc.project.infrasight.statscaptureservice.services.StatsService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StatsServiceImpl implements StatsService {

	@Autowired
	private ProjectDao projectDao;
	@Autowired
	private ServerService serverService;
	@Autowired
	private ProphetServiceFeignClient prophetService;
	@Autowired
	private MLServiceFeignClient mlServiceFeignClient;

	@Override
	public void storeDiscAndIOStat(String host, List<String> discResponse, List<String> ioResponseLines)
			throws ServiceNotFoundException {
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
			for (int lineNo = 6; lineNo < ioLines.length; lineNo++) {
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
	public void storeCPUAndRAM(String host, List<String> cpuLine, List<String> ramLine, List<String> swapLine,
			List<String> loadLine) throws ServiceNotFoundException {
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
				log.error("{}) {}: {} - {}", server.getName(), server.getRamLimit(), ((double) (usedRam / totalRam)),
						cpuUse.toString());
				LocalDate today = LocalDate.now();
//				if(server.getRamLimit() < ramusedPercentage && (server.getLastRamNotificationSent() == null || server.getLastRamNotificationSent().isBefore(today))) {
				if (server.getRamLimit() < ramusedPercentage) {
					MailingModel mail = new MailingModel();
					mail.setTo(server.getServerAdmin().getCompanyEmail());
					mail.setSubject("Update on your server " + server.getName());
					mail.setBody("Your server " + server.getName() + " has over throttled with RAM utilization of "
							+ ramusedPercentage + "% and corresponding CPU usage is " + cpuUse + "%");
					prophetService.sendMail(mail);
//					server.setLastRamNotificationSent(today);
				}
//				}
//				if(server.getCpuLimit() < cpuUse && (server.getLastCpuNotificationSent() == null || server.getLastCpuNotificationSent().isBefore(today))) {
				if (server.getCpuLimit() < cpuUse) {
					MailingModel mail = new MailingModel();
					mail.setTo(server.getServerAdmin().getCompanyEmail());
					mail.setSubject("Update on your server " + server.getName());
					mail.setBody("Your server " + server.getName() + " has over throttled with CPU utilization of "
							+ cpuUse + "% and corresponding RAM usage is " + ramusedPercentage + "%");
					prophetService.sendMail(mail);
//					server.setLastCpuNotificationSent(today);
				}
//				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("Error Sending mail.");
			}
			try {
				Long totalRam = stats.getTotalRam();
				Long freeRam = stats.getAvailableRam();
				Long usedRam = totalRam - freeRam;
				double ramusedPercentage = (usedRam.doubleValue() / totalRam.doubleValue()) * 100D;
				double cpuUse = stats.getCpuPerformance();
				double load = stats.getServerLoad();				
				List<DiscStatsEntity> disc = new ArrayList<>();
				disc.addAll(server.getDiscStats());
				List<DiscMountEntity> mount = new ArrayList<>();
				mount.addAll(disc.get(0).getDiscMounts());
				long discUse = Long.valueOf(mount.get(0).getUse().split("%")[0]);
				Integer uptime = 50;
				
				try {
					String res = mlServiceFeignClient.suggestScaling((int) cpuUse, (int) discUse, (int) ramusedPercentage, (int) load, (int) uptime);
					log.error("res: {}", res);
					if(res == "down") {
						MailingModel mail = new MailingModel();
						mail.setTo(server.getServerAdmin().getCompanyEmail());
						mail.setBody("Scaling suggestion");
						mail.setSubject("your server " + server.getName() + " needs to scale down consider scaling down.");
						prophetService.sendMail(mail);
					}
					if(res == "up") {
						MailingModel mail = new MailingModel();
						mail.setTo(server.getServerAdmin().getCompanyEmail());
						mail.setBody("Scaling suggestion");
						mail.setSubject("your server " + server.getName() + " needs to scale up consider scaling up.");
						prophetService.sendMail(mail);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			} catch(Exception e) {
				
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

	@Override
	public List<ProjectEntity> storeProject(String serverId, List<String> serverLine) throws ServiceNotFoundException {
		ServerEntity server = serverService.findOne(serverId);
		String projectLine = serverLine.get(0);
		log.error(projectLine);
		List<ProjectEntity> returnValue = new ArrayList<>();
		String[] lines = projectLine.split("\n");
		for (String line : lines) {
			String[] words = line.split("\\s+");
			log.error("CPU: {} RAM: {} Project: {}", words[2], words[3], words[11]);

			String projectId = "";
			String regex = ".*\\/([^\\/]+)\\/node_modules\\/(.*)?";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(words[11]);
			if (matcher.matches()) {
				String result = matcher.group(1);
				projectId = result;
			} else {
				projectId = "UNKNOWN";
			}
			ProjectEntity project = projectDao.findById(projectId).orElse(null);
			if (project == null) {
				project = new ProjectEntity();
				project.setId(projectId);
				project.setProgrammingLanguage("Java Script");
				project.setFramework("Node JS");
				project.setInstalledOn(server);
			}
			StatsEntity statsEntity = new StatsEntity();
			statsEntity.setCpuPerformance(Double.valueOf(words[2]));
			statsEntity.setRamPerformance(Double.valueOf(words[3]));

			project.getRamCpuStats().add(statsEntity);
			ProjectEntity savedProject = projectDao.save(project);
			server.getProjects().add(savedProject);
			serverService.save(server);
		}

		log.error(returnValue.toString());

		return returnValue;
	}

}
