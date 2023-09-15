# Infrasight-Spring-Boot
This is an Spring Boot microservice application that has provisions to connect through SSh and Grab server stats.

# CPU and RAM
```bash
@AllArgsConstructor
@RequiredArgsConstructor
public class EnvironmentUpdateTask implements Runnable {

	private ServerConnectionHelper connectionHelper;

	private EnvStatusRepository envStatusRepo ;

	private Environment env;
	
	private DiscUtilizationService discService;

	private String sshKeyPath;
	private String sshKeyPassphrase;
	private static String MEMORY_CMD="cat /proc/meminfo | head -3";
	private static String CPU_CMD="mpstat 1 1";
	private static String DISC_CMD="df -h";
	
	private static Logger log = LogManager.getLogger(EnvironmentUpdateTask.class);

	@Override
	public void run() {
		Session session = null;
		try {
			if(env.getUserList()!=null && !env.getUserList().isEmpty())
				session = connectionHelper.getSession(env.getIp(), env.getUserList().get(0).getUserName(), sshKeyPath, sshKeyPassphrase);
			else 
				log.info("No user setup for env :"+env.getIp()+" . So not able connect and collect data");
		} catch (Exception e){
			log.error("Received error at ServiceUpdateHelper.populateServices() : "+e);
		}
		try{
			if(session!=null) {				
				List<String> ramresult=connectionHelper.executeCommand(MEMORY_CMD, session);
				String[] ramlines = ramresult.get(0).split("\n");
				String totlram[]=ramlines[0].split("\\s+");
				long ramTotal=Long.parseLong(totlram[1]);
				String memavail[]=ramlines[2].split("\\s+");
				long ramAvailable=Long.parseLong(memavail[1]);	

				List<String> cpuresult=connectionHelper.executeCommand(CPU_CMD, session);
				String[] cpulines;
				if(cpuresult.size()==2)
					cpulines=cpuresult.get(1).split("\n");
				else
					cpulines=cpuresult.get(0).split("\n");

				String[] strarray=cpulines[3].split("\\s+");
				Double cpuPerformance=100-(Double.parseDouble(strarray[11]));
				DecimalFormat twoDForm = new DecimalFormat("#.##");
				cpuPerformance= Double.valueOf(twoDForm.format(cpuPerformance));
				EnvStatus envStatus=new EnvStatus(ramTotal,ramAvailable,cpuPerformance,LocalDateTime.now(), env);
				envStatusRepo.save(envStatus);
				
				List<String> discSpace = connectionHelper.executeCommand(DISC_CMD, session);
				discService.saveDiscUtilization(env, discSpace);
				
				log.info("Env Status updated : "+ envStatus);
			}
		}catch(Exception e){
			log.error("Received error at ServiceUpdateHelper.populateServices() : "+e);
		} finally {
			if(session!=null)
				session.disconnect();
		}
	}
}
```
# Disc Utilization
```bash
public void saveDiscUtilization(Environment environment, List<String> datas) {

//		log.error("{}) Taken: {}", environment.getEnvId(), !checkIfDataExists(environment.getEnvId(), LocalDate.now()));
		if (!checkIfDataExists(environment.getEnvId(), LocalDate.now())) {
			DiscUtilization newData = new DiscUtilization(environment);
			DiscUtilization savedDiscUtilization = discRepo.save(newData);

			Set<DiscMount> discMounts = new HashSet<>();
			for (String data : datas) {
				String[] lines = data.split("\n");
				for (int i = 1; i < lines.length; i++) {
					String line = lines[i];
					String[] words = line.replaceAll("\\s{2,}", " ").split(" ");
					if (words.length >= 6) {
						String fileSystem = words[0];
						String size = words[1];
						String used = words[2];
						String avail = words[3];
						String use = words[4];
						String mount = words[5];
						DiscMount discMount = new DiscMount(fileSystem, size, used, avail, use, mount,
								savedDiscUtilization);
						DiscMount saved = discMountsRepo.save(discMount);
						discMounts.add(saved);
//						log.error("{} - {} - {} - {} - {} - {} - {} - {}", new Long(i), fileSystem, size, used, avail, use, mount, newData);	
					}
				}
//				log.error("discMounts: {}", discMounts.size());
			}
//			List<DiscMount> savedDiscMount = discMountsRepo.saveAllAndFlush(discMounts);
			newData.getDiscMounts().addAll(discMounts);
//			log.error("===>{}", newData.toString());
			discRepo.save(newData);
		}

	}
```

