package gmc.project.infrasight.statscaptureservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private ServerService serverService;
	@Autowired
	private EncryptionService encrypt;
	@Autowired
	private SSHConnectionService connectionService;
	
	public void captureMemoryAndCPUStats() throws Exception {
		List<ServerEntity> servers = serverService.findAll();
		for(ServerEntity server : servers) {
			try {
				Session serverSession = connectionService.getSession(server.getHost(), server.getPort(), server.getUsername(), encrypt.decrypt(server.getPassword()));
				List<String> ramResponseLines = connectionService.executeCommand(RAM_UTILIZATION_COMMAND, serverSession);
				ramResponseLines.remove(0);
				for(String line : ramResponseLines) {
					log.error(line);
				}
								
				List<String> cpuResponseLines = connectionService.executeCommand(CPU_UTILIZATION_COMMAND, serverSession);
				cpuResponseLines.remove(0);
				for(String line : cpuResponseLines) {
					log.error(line);
				}
			} catch(Exception e) {
				log.error("CPU and RAM: The server {} is down.", server.getName());
			}
		}
	}
	
	public void captureDiscUtilization() throws Exception {
		List<ServerEntity> servers = serverService.findAll();
		for(ServerEntity server : servers) {
			try {
				Session serverSession = connectionService.getSession(server.getHost(), server.getPort(), server.getUsername(), encrypt.decrypt(server.getPassword()));
				List<String> responseLines = connectionService.executeCommand(DISC_UTILIZATION_COMMAND, serverSession);
				responseLines.remove(0);
				for(String line : responseLines) {
					log.error(line);
				}
			} catch(Exception e) {
				log.error("Disc Utilization: The server {} is down.", server.getName());
			}
		}
	}
	
}
