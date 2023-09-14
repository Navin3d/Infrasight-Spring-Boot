package gmc.project.infrasight.statscaptureservice.services.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import gmc.project.infrasight.statscaptureservice.services.SSHConnectionService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SSHConnectionServiceImpl implements SSHConnectionService {

	@Override
	public Session getSession(String host, Integer port, String userName, String password) throws Exception {
		Properties jschConfig = new Properties();
		jschConfig.put("StrictHostKeyChecking", "no");
		JSch jsch = new JSch();
		
		log.info("Attempting to get session of {} with username {}.", host, userName);
		Session session = jsch.getSession(userName, host, port);
		session.setConfig(jschConfig);
		session.setPassword(password);
		session.connect();
		log.info("Successfully connected with session of {} with username {}.", host, userName);
		
		return session;
	}

	@Override
	public List<String> executeCommand(String command, Session session) {
		Channel channel = null;
		List<String> response = new ArrayList<>();
		
		try {
			log.info("Attempting to execute command {}.", command);
			channel = session.openChannel("exec");
			((ChannelExec)channel).setCommand(command);
			channel.setInputStream(null);
			((ChannelExec)channel).setErrStream(System.err);

			InputStream in = channel.getInputStream();
			channel.connect();
			
			byte[] lineByte = new byte[1024];
			while(true) {
				while(in.available() > 0) {
					int lineLength = in.read(lineByte, 0, 1024);
					if(lineLength < 0) break;
					String lineString = new String(lineByte, 0, lineLength);
					if(lineString.isEmpty()) continue;
					response.add(lineString);
				}
				if(channel.isClosed()) {
					if(response.size() == 0)
						log.info("Exit status = {} while running command {}.", channel.getExitStatus(), command);
					break;
				}
				try {
					Thread.sleep(1000);
				} catch(Exception e) {}
			}
		} catch (Exception e) {
			log.error("Facing exception while running command {} with log {}.", command, e.getMessage());
		}
		if(response.size() == 0)
			log.info("Exit status = {} while running command {}.", channel.getExitStatus(), command);
		channel.disconnect();
		return response;
	}

}
