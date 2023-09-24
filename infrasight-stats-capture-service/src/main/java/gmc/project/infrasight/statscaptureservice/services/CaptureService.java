package gmc.project.infrasight.statscaptureservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.Session;

import gmc.project.infrasight.statscaptureservice.entities.ServerEntity;
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

	@Autowired
	private ServerService serverService;
	@Autowired
	private StatsService statsService;
	@Autowired
	private EncryptionService encrypt;
	@Autowired
	private SSHConnectionService connectionService;
	
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
			} catch(Exception e) {
				e.printStackTrace();
				log.error("CPU and RAM: The server {} is down.", server.getName());
				statsService.storeCPUAndRAM(serverId, null, null, null, null);
			}
		}
	}
	
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
	
}
