package gmc.project.infrasight.statscaptureservice.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Stream;

import javax.management.ServiceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.Session;

import gmc.project.infrasight.statscaptureservice.concurreny.DiscAndIORunnable;
import gmc.project.infrasight.statscaptureservice.concurreny.DiscIOStringModel;
import gmc.project.infrasight.statscaptureservice.concurreny.MemoryCPURunnable;
import gmc.project.infrasight.statscaptureservice.concurreny.MemoryCPUStringModel;
import gmc.project.infrasight.statscaptureservice.daos.TaskDao;
import gmc.project.infrasight.statscaptureservice.entities.ServerEntity;
import gmc.project.infrasight.statscaptureservice.entities.TaskEntity;
import gmc.project.infrasight.statscaptureservice.models.MailingModel;
import gmc.project.infrasight.statscaptureservice.utils.SSHConnectionUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CaptureService {

	@Autowired
	private ProphetServiceFeignClient prophetService;
	@Autowired
	private ServerService serverService;
	@Autowired
	private StatsService statsService;
	@Autowired
	private EncryptionService encrypt;
	@Autowired
	private TaskDao taskDao;

	@Value("${app.totalThreadLimit}")
	private static Integer totalThreadLimit;

	public static ThreadPoolExecutor globalThreadPool = (ThreadPoolExecutor) Executors
			.newFixedThreadPool(totalThreadLimit);

	@Scheduled(fixedDelay = 300000, initialDelay = 300000)
	public void captureMemoryAndCPUStats() throws Exception {
		List<ServerEntity> servers = serverService.findAll();

		Stream<CompletableFuture<MemoryCPURunnable>> completableStats = servers.stream().map(server -> {
			CompletableFuture<MemoryCPURunnable> serversFuture = CompletableFuture.supplyAsync(
					() -> new MemoryCPURunnable(server, encrypt, statsService, prophetService), globalThreadPool);
			return serversFuture;
		});

		completableStats.forEachOrdered(thread -> {
			MemoryCPURunnable completedFuture = thread.join();
			MemoryCPUStringModel strings = completedFuture.get();
			try {
				completedFuture.statsHelper(strings);
			} catch (ServiceNotFoundException e) {
				// Chances of getting this error is 1% can be occured inly if someone reemoves
				// server from db during capturing process.
				e.printStackTrace();
			}
		});

	}

	@Scheduled(fixedDelay = 1000000, initialDelay = 1000000)
	public void captureDiscAndIOUtilization() throws Exception {
		List<ServerEntity> servers = serverService.findAll();

		Stream<CompletableFuture<DiscAndIORunnable>> completableStats = servers.stream().map(server -> {
			CompletableFuture<DiscAndIORunnable> serversFuture = CompletableFuture
					.supplyAsync(() -> new DiscAndIORunnable(server, encrypt, statsService), globalThreadPool);
			return serversFuture;
		});

		completableStats.forEachOrdered(thread -> {
			DiscAndIORunnable completedFuture = thread.join();
			DiscIOStringModel strings = completedFuture.get();
			try {
				completedFuture.discIOThreadHelper(strings);
			} catch (ServiceNotFoundException e) {
				// Chances of getting this error is 1% can be occured inly if someone reemoves
				// server from db during capturing process.
				e.printStackTrace();
			}
		});
	}

	@Scheduled(fixedDelay = 300000, initialDelay = 300000)
	public void runScheduledtask() {
		List<TaskEntity> tasks = taskDao.findByAtEndOfDay(true);

		Stream<CompletableFuture<Void>> completableTasks = tasks.stream().map(task -> {
			CompletableFuture<Void> serversFuture = CompletableFuture.runAsync(() -> {
				ServerEntity server = task.getRunOnServers();
				try {
					Session serverSession = SSHConnectionUtils.getSession(server.getHost(), server.getPort(),
							server.getUsername(), encrypt.decrypt(server.getPassword()));
					List<String> response = SSHConnectionUtils.executeCommand(task.getCommand(), serverSession);
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
			}, globalThreadPool);
			return serversFuture;
		});

		completableTasks.forEachOrdered(thread -> {
			thread.join();
		});
	}

}
