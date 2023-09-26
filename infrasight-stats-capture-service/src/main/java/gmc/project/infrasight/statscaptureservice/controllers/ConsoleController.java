package gmc.project.infrasight.statscaptureservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gmc.project.infrasight.statscaptureservice.models.CommandModel;
import gmc.project.infrasight.statscaptureservice.models.ResponseModel;
import gmc.project.infrasight.statscaptureservice.services.ConsoleService;

@RequestMapping(path = "/console")
@RestController
public class ConsoleController {
	
	@Autowired
	private ConsoleService consoleService;
	
	@PostMapping
	public ResponseEntity<ResponseModel<String>> execute(@RequestBody CommandModel command) {
		ResponseModel<String> body = new ResponseModel<>("Successfully executed command.", true);
		HttpStatus status = HttpStatus.OK;
		try {
			List<String> response = consoleService.executeInServer(command.getServerId(), command.getCommand());
			body.setDatas(response);
		} catch (Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			body.setMessage("Restricted Command.");
			body.setStatus(false);
			body.setError(e.getMessage());
		}
		return ResponseEntity.status(status).body(body);
	}
	
	@GetMapping(path ="/scan/{serverId}")
	public ResponseEntity<ResponseModel<String>> runVulnaribilityScan(@PathVariable String serverId) {
		ResponseModel<String> body = new ResponseModel<>("Running scan wait for response.", true);
		HttpStatus status = HttpStatus.OK;
		try {
			List<String> returnValue = consoleService.runSecurityCheck(serverId);
			body.setDatas(returnValue);
		} catch (Exception e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			body.setMessage("Restricted Command.");
			body.setStatus(false);
			body.setError(e.getMessage());
		}
		return ResponseEntity.status(status).body(body);
	}
 
}
