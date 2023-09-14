package gmc.project.infrasight.serverservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gmc.project.infrasight.serverservice.models.CreateServerModel;
import gmc.project.infrasight.serverservice.services.ServerService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(path = "/server")
@RestController
public class ServerController {
	
	@Autowired
	private ServerService server;
	
	@PostMapping
	private ResponseEntity<String> addToList(@RequestBody CreateServerModel createServer) {
		HttpStatus responseCode = HttpStatus.OK;
		String body = "Server added to monitoring list,";
		try {
			server.addToMonitorList(createServer);
		} catch (Exception e) {
			body = e.getMessage();
			log.error(body);
			e.printStackTrace();
			responseCode = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return ResponseEntity.status(responseCode).body(body);
	}

}
