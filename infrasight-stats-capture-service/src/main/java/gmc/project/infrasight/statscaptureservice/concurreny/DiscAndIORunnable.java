package gmc.project.infrasight.statscaptureservice.concurreny;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import javax.management.ServiceNotFoundException;

import com.jcraft.jsch.Session;

import gmc.project.infrasight.statscaptureservice.entities.ServerEntity;
import gmc.project.infrasight.statscaptureservice.services.CaptureService;
import gmc.project.infrasight.statscaptureservice.services.EncryptionService;
import gmc.project.infrasight.statscaptureservice.services.StatsService;
import gmc.project.infrasight.statscaptureservice.utils.SSHConnectionUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DiscAndIORunnable implements Supplier<Object> {
	
	private final String IO_READ_WRITE_COMMAND = "iostat";
	private final String DISC_UTILIZATION_COMMAND = "df -H";
	
	private ServerEntity server;
	private EncryptionService encrypt;
	private StatsService statsService;

	public DiscAndIORunnable(ServerEntity server, EncryptionService encrypt, StatsService statsService) {
		this.server = server;
		this.encrypt = encrypt;
		this.statsService = statsService;
	}

	@Override
	public DiscIOStringModel get() {
		DiscIOStringModel returnValue = new DiscIOStringModel();
		try {
			Session serverSession = SSHConnectionUtils.getSession(server.getHost(), server.getPort(),
					server.getUsername(), encrypt.decrypt(server.getPassword()));

			CompletableFuture<List<String>> responseLinesFuture = CompletableFuture.supplyAsync(
					() -> SSHConnectionUtils.executeCommand(DISC_UTILIZATION_COMMAND, serverSession),
					CaptureService.globalThreadPool);
			CompletableFuture<List<String>> ioResponseLinesFuture = CompletableFuture.supplyAsync(
					() -> SSHConnectionUtils.executeCommand(IO_READ_WRITE_COMMAND, serverSession),
					CaptureService.globalThreadPool);

			List<String> responseLines = responseLinesFuture.get();
			List<String> ioResponseLines = ioResponseLinesFuture.get();
			
			returnValue.setDiscResponseLines(responseLines);
			returnValue.setIoResponseLines(ioResponseLines);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Disc Utilization: The server {} is down.", server.getName());
		}
		
		return returnValue;
	}
	
	public void discIOThreadHelper(DiscIOStringModel discIOStringModel) throws ServiceNotFoundException {
		String serverId = server.getId();
		statsService.storeDiscAndIOStat(serverId, discIOStringModel.getDiscResponseLines(), discIOStringModel.getIoResponseLines());
	}

}
