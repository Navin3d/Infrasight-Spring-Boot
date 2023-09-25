package gmc.project.infrasight.serverservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gmc.project.infrasight.serverservice.models.ResponseModel;
import gmc.project.infrasight.serverservice.models.ServerModel;
import gmc.project.infrasight.serverservice.services.ServerService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(path = "/server")
@RestController
public class ServerController {
	
	@Autowired
	private ServerService server;
	
	@GetMapping(path = "/{id}/user/{userId}")
	private ResponseEntity<ResponseModel<ServerModel>> getServer(@PathVariable String id, @PathVariable String userId) {
		HttpStatus responseCode = HttpStatus.OK;
		ResponseModel<ServerModel> body = new ResponseModel<>("Server data retrived successfully.", true);
		try {
			body.setData(server.getServer(id, userId));
		} catch (Exception e) {
			body.setMessage("Error retriving server data.");
			body.setStatus(false);
			body.setError(e.getMessage());
			responseCode = HttpStatus.INTERNAL_SERVER_ERROR;
			e.printStackTrace();
		}
		return ResponseEntity.status(responseCode).body(body);
	}
	
	@PostMapping
	private ResponseEntity<String> addToList(@RequestBody ServerModel createServer) {
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
	
	@PostMapping(path = "/update")
	private ResponseEntity<String> updateServer(@RequestBody ServerModel createServer) {
		HttpStatus responseCode = HttpStatus.OK;
		String body = "Server details Updated,";
		try {
			server.updateServerDetails(createServer);
		} catch (Exception e) {
			body = e.getMessage();
			log.error(body);
			e.printStackTrace();
			responseCode = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return ResponseEntity.status(responseCode).body(body);
	}

}
