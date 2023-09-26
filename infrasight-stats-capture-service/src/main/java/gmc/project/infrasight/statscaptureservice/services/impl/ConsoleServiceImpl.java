package gmc.project.infrasight.statscaptureservice.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.Session;

import gmc.project.infrasight.statscaptureservice.entities.ServerEntity;
import gmc.project.infrasight.statscaptureservice.services.ConsoleService;
import gmc.project.infrasight.statscaptureservice.services.EncryptionService;
import gmc.project.infrasight.statscaptureservice.services.SSHConnectionService;
import gmc.project.infrasight.statscaptureservice.services.ServerService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConsoleServiceImpl implements ConsoleService {
	
	@Autowired
	private ServerService serverService;
	@Autowired
	private EncryptionService encryptionService;
	@Autowired
	private SSHConnectionService sshService;

	@Override
	public List<String> executeInServer(String serverId, String command) throws Exception {
		ServerEntity server = serverService.findOne(serverId);
		String host = server.getHost();
		String username = server.getUsername();
		String password = encryptionService.decrypt(server.getPassword());
		Integer port = server.getPort();
		Session serverSession = sshService.getSession(host, port, username, password);
		List<String> rawLogs = sshService.executeCommand(command, serverSession);
		List<String> returnValue = new ArrayList<>();
		for(String logTxt: rawLogs) {
			List<String> doubleBreak = new ArrayList<>();
			doubleBreak.addAll(Arrays.asList(logTxt.split("\n\n")));
			for(String line: doubleBreak) {
				returnValue.addAll(Arrays.asList(line.split("\n")));
			}
		}
		log.error("Total {} lines.", returnValue.size());
		return rawLogs;
	}
	
	@Override
	public List<String> extractVulnerabilities(String text) {
        List<String> vulnerabilities = new ArrayList<>();
        
        String pattern = "^\\s*![^\\n]*";
        
        Pattern regexPattern = Pattern.compile(pattern, Pattern.MULTILINE);
        
        Matcher matcher = regexPattern.matcher(text);
        
        while (matcher.find()) {
            vulnerabilities.add(matcher.group());
        }
        
        return vulnerabilities;
    }

	@Override
	public List<String> runSecurityCheck(String serverId) throws Exception {
		ServerEntity server = serverService.findOne(serverId);
		String host = server.getHost();
		String username = server.getUsername();
		String password = encryptionService.decrypt(server.getPassword());
		Integer port = server.getPort();
		Session serverSession = sshService.getSession(host, port, username, password);
		List<String> rawLogs = sshService.executeCommand("clamscan -r .", serverSession);
		List<String> returnValue = Arrays.asList(rawLogs.get(0).split("\n"));
		log.error("Total {} lines.", returnValue.size());
		return rawLogs;
	}

}
