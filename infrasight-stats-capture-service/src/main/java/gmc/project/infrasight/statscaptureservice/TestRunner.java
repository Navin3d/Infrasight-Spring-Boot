package gmc.project.infrasight.statscaptureservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import gmc.project.infrasight.statscaptureservice.services.CaptureService;

@Component
public class TestRunner implements CommandLineRunner {
	
	@Autowired
	private CaptureService capService;

	@Override
	public void run(String... args) throws Exception {
		capService.captureDiscAndIOUtilization();

		capService.captureMemoryAndCPUStats();
		
		capService.runScheduledtask();
	}

}
