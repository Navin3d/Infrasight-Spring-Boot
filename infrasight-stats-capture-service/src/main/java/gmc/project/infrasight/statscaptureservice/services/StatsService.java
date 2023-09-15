package gmc.project.infrasight.statscaptureservice.services;

import java.util.List;

import javax.management.ServiceNotFoundException;

public interface StatsService {
	public void storeDiscStat(String host, List<String> discLines) throws ServiceNotFoundException;
	public void storeCPUAndRAM(String host, List<String> cpuLine, List<String> ramLine) throws ServiceNotFoundException;
}
