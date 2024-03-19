package gmc.project.infrasight.statscaptureservice.concurreny;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import javax.management.ServiceNotFoundException;

import com.jcraft.jsch.Session;

import gmc.project.infrasight.statscaptureservice.entities.ServerEntity;
import gmc.project.infrasight.statscaptureservice.models.MailingModel;
import gmc.project.infrasight.statscaptureservice.services.CaptureService;
import gmc.project.infrasight.statscaptureservice.services.EncryptionService;
import gmc.project.infrasight.statscaptureservice.services.ProphetServiceFeignClient;
import gmc.project.infrasight.statscaptureservice.services.StatsService;
import gmc.project.infrasight.statscaptureservice.utils.SSHConnectionUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemoryCPURunnable implements Supplier<MemoryCPUStringModel> {

	private final String CPU_UTILIZATION_COMMAND = "mpstat 1 1";
	private final String RAM_UTILIZATION_COMMAND = "cat /proc/meminfo | head -3";
	private final String LOAD_AND_UPTIME_COMMAND = "uptime";
	private final String SWAP_STAT_COMMAND = "free -m";
	private final String PROJECT_STATS_COMMAND = "ps aux | grep node";

	private ServerEntity server;
	private EncryptionService encrypt;
	private StatsService statsService;
	private ProphetServiceFeignClient prophetService;

	public MemoryCPURunnable(ServerEntity server, EncryptionService encrypt, StatsService statsService,
			ProphetServiceFeignClient prophetService) {
		this.server = server;
		this.encrypt = encrypt;
		this.statsService = statsService;
		this.prophetService = prophetService;
	}

	@Override
	public MemoryCPUStringModel get() {
		MemoryCPUStringModel returnValue = new MemoryCPUStringModel();
		String host = server.getHost();

		AtomicReference<Session> serverSession = new AtomicReference<Session>();
		try {
			serverSession.set(SSHConnectionUtils.getSession(host, server.getPort(), server.getUsername(),
					encrypt.decrypt(server.getPassword())));
			
			CompletableFuture<List<String>> ramResponseLinesFuture = CompletableFuture
					.supplyAsync(() -> SSHConnectionUtils.executeCommand(RAM_UTILIZATION_COMMAND, serverSession.get()), CaptureService.globalThreadPool);
			CompletableFuture<List<String>> cpuResponseLinesFuture = CompletableFuture
					.supplyAsync(() -> SSHConnectionUtils.executeCommand(CPU_UTILIZATION_COMMAND, serverSession.get()), CaptureService.globalThreadPool);
			CompletableFuture<List<String>> swapResponseLinesFuture = CompletableFuture
					.supplyAsync(() -> SSHConnectionUtils.executeCommand(SWAP_STAT_COMMAND, serverSession.get()), CaptureService.globalThreadPool);
			CompletableFuture<List<String>> loadResponseLineFuture = CompletableFuture
					.supplyAsync(() -> SSHConnectionUtils.executeCommand(LOAD_AND_UPTIME_COMMAND, serverSession.get()), CaptureService.globalThreadPool);
			CompletableFuture<List<String>> projectResponseLineFuture = CompletableFuture
					.supplyAsync(() -> SSHConnectionUtils.executeCommand(PROJECT_STATS_COMMAND, serverSession.get()), CaptureService.globalThreadPool);
		
			try {
				List<String> ramResponseLines = ramResponseLinesFuture.get();
				returnValue.setRamResponseLines(ramResponseLines);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			try {
				List<String> cpuResponseLines = cpuResponseLinesFuture.get();
				returnValue.setCpuResponseLines(cpuResponseLines);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			try {
				List<String> swapResponseLines = swapResponseLinesFuture.get();
				returnValue.setSwapResponseLines(swapResponseLines);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			try {
				List<String> loadResponseLine = loadResponseLineFuture.get();
				returnValue.setLoadResponseLine(loadResponseLine);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			try {
				List<String> projectResponseLine = projectResponseLineFuture.get();
				returnValue.setProjectResponseLine(projectResponseLine);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			log.error("CPU and RAM: The server {} is down.", server.getName());
			log.error("Failed getting session for server {} with trace {}", host, e.getMessage());
			LocalDate today = LocalDate.now();
			if (server.getLastDownNotificationSent() == null || server.getLastDownNotificationSent().isBefore(today))
				try {
					MailingModel mail = new MailingModel();
					mail.setTo(server.getServerAdmin().getCompanyEmail());
					mail.setSubject("Update on your server " + server.getName());
					mail.setBody("Your server " + server.getName() + " is down.");
					prophetService.sendMail(mail);
					server.setLastDownNotificationSent(today);
				} catch (Exception ex) {
					ex.printStackTrace();
					log.error("Error sending mail: {}.", ex.getMessage());
				}
		}

		return returnValue;
	}

	public void statsHelper(MemoryCPUStringModel memoryCpuStrings) throws ServiceNotFoundException {
		String serverId = server.getId();

		try {
			statsService.storeCPUAndRAM(serverId, memoryCpuStrings.getCpuResponseLines(), memoryCpuStrings.getRamResponseLines(), memoryCpuStrings.getSwapResponseLines(),
					memoryCpuStrings.getLoadResponseLine());
			statsService.storeProject(serverId, memoryCpuStrings.getProjectResponseLine());
		} catch (Exception e) {
			e.printStackTrace();

			statsService.storeCPUAndRAM(serverId, null, null, null, null);
		}
		
	}

}
