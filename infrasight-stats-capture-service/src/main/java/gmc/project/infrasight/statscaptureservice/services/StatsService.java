package gmc.project.infrasight.statscaptureservice.services;

import java.util.List;

import javax.management.ServiceNotFoundException;

import gmc.project.infrasight.statscaptureservice.entities.ProjectEntity;

public interface StatsService {
	public void storeDiscAndIOStat(String host, List<String> discLines, List<String> ioResponseLines) throws ServiceNotFoundException;
	public void storeCPUAndRAM(String host, List<String> cpuLine, List<String> ramLine, List<String> swapLine, List<String> loadLine) throws ServiceNotFoundException;
	public List<ProjectEntity> storeProject(String serverId, List<String> serverLine) throws ServiceNotFoundException;
}
