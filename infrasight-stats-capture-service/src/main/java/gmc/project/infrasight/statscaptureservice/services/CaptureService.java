package gmc.project.infrasight.statscaptureservice.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.Session;

import gmc.project.infrasight.statscaptureservice.daos.TaskDao;
import gmc.project.infrasight.statscaptureservice.entities.ServerEntity;
import gmc.project.infrasight.statscaptureservice.models.MailingModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CaptureService {
	
	private final String CPU_UTILIZATION_COMMAND = "mpstat 1 1"; 
	private final String RAM_UTILIZATION_COMMAND = "cat /proc/meminfo | head -3"; 
	private final String DISC_UTILIZATION_COMMAND = "df -H"; 
	private final String LOAD_AND_UPTIME_COMMAND = "uptime";
	private final String IO_READ_WRITE_COMMAND = "iostat";
	private final String SWAP_STAT_COMMAND = "free -m";
	private final String PROJECT_STATS_COMMAND = "ps aux | grep node";

	@Autowired
	private ProphetServiceFeignClient prophetService;
	@Autowired
	private ServerService serverService;
	@Autowired
	private StatsService statsService;
	@Autowired
	private EncryptionService encrypt;
	@Autowired
	private SSHConnectionService connectionService;
	@Autowired
	private TaskDao taskDao;
	
	@Scheduled(fixedDelay = 300000, initialDelay = 300000)
	public void captureMemoryAndCPUStats() throws Exception {
		List<ServerEntity> servers = serverService.findAll();
		for(ServerEntity server : servers) {
			String host = server.getHost();
			String serverId = server.getId();
			try {
				Session serverSession = connectionService.getSession(host, server.getPort(), server.getUsername(), encrypt.decrypt(server.getPassword()));
				List<String> ramResponseLines = connectionService.executeCommand(RAM_UTILIZATION_COMMAND, serverSession);								
				List<String> cpuResponseLines = connectionService.executeCommand(CPU_UTILIZATION_COMMAND, serverSession);
				List<String> swapResponseLines = connectionService.executeCommand(SWAP_STAT_COMMAND, serverSession);
				List<String> loadResponseLine = connectionService.executeCommand(LOAD_AND_UPTIME_COMMAND, serverSession);
				statsService.storeCPUAndRAM(serverId, cpuResponseLines, ramResponseLines, swapResponseLines, loadResponseLine);
				List<String> projectResponseLine = connectionService.executeCommand(PROJECT_STATS_COMMAND, serverSession);
				statsService.storeProject(serverId, projectResponseLine);
			} catch(Exception e) {
				LocalDate today = LocalDate.now();
				if(server.getLastDownNotificationSent() == null || server.getLastDownNotificationSent().isBefore(today))
					try {
						MailingModel mail = new MailingModel();
						mail.setTo(server.getServerAdmin().getCompanyEmail());
						mail.setSubject("Update on your server " + server.getName());
						mail.setBody("Your server " + server.getName() + " is down.");
						prophetService.sendMail(mail);
//						server.setLastDownNotificationSent(today);
					} catch (Exception ex) {
						ex.printStackTrace();
						log.error("Error sending mail: {}.", ex.getMessage());
					}
				e.printStackTrace();
				log.error("CPU and RAM: The server {} is down.", server.getName());
				statsService.storeCPUAndRAM(serverId, null, null, null, null);
			}
		}
	}
	
	@Scheduled(fixedDelay = 1000000, initialDelay = 1000000)
	public void captureDiscAndIOUtilization() throws Exception {
		List<ServerEntity> servers = serverService.findAll();
		for(ServerEntity server : servers) {
			String serverId = server.getId();
			try {
				Session serverSession = connectionService.getSession(server.getHost(), server.getPort(), server.getUsername(), encrypt.decrypt(server.getPassword()));
				List<String> responseLines = connectionService.executeCommand(DISC_UTILIZATION_COMMAND, serverSession);
				List<String> ioResponseLines = connectionService.executeCommand(IO_READ_WRITE_COMMAND, serverSession);
				statsService.storeDiscAndIOStat(serverId, responseLines, ioResponseLines);
			} catch(Exception e) {
				e.printStackTrace();
				log.error("Disc Utilization: The server {} is down.", server.getName());
			}
		}
	}
	
	@Scheduled(fixedDelay = 300000, initialDelay = 300000)
	public void runScheduledtask() {
		taskDao.findByAtEndOfDay(true).forEach(task -> {
			ServerEntity server = task.getRunOnServers();
			Session serverSession;
			try {
				serverSession = connectionService.getSession(server.getHost(), server.getPort(), server.getUsername(), encrypt.decrypt(server.getPassword()));
				List<String> response = connectionService.executeCommand(task.getCommand(), serverSession);
				try {
					MailingModel mail = new MailingModel();
					mail.setTo(server.getServerAdmin().getCompanyEmail());
					mail.setSubject("Update on your server " + server.getName());
					mail.setBody(response.get(0));
					prophetService.sendMail(mail);
				} catch (Exception ex) {
					ex.printStackTrace();
					log.error("Error sending mail: {}.", ex.getMessage());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});;
	}
	
}
