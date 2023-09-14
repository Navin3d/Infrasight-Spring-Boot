package gmc.project.infrasight.statscaptureservice.services;

import java.util.List;

import com.jcraft.jsch.Session;

public interface SSHConnectionService {
	public Session getSession(String host, Integer port, String userName, String passsword) throws Exception;
	public List<String> executeCommand(String command, Session session);
}
