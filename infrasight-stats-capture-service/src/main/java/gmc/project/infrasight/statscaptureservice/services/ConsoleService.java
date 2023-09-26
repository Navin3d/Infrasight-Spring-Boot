package gmc.project.infrasight.statscaptureservice.services;

import java.util.List;

public interface ConsoleService {
	public List<String> executeInServer(String serverId, String command) throws Exception;
	public List<String> extractVulnerabilities(String text);
	public List<String> runSecurityCheck(String serverId) throws Exception;
}
