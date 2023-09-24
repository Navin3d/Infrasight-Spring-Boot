package gmc.project.infrasight.serverservice.controllers;

import java.rmi.AccessException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gmc.project.infrasight.serverservice.models.ResponseModel;
import gmc.project.infrasight.serverservice.services.AccessService;

@RequestMapping(path = "/access")
@RestController
public class AccessController {
	
	@Autowired
	private AccessService accessService;
	
	@GetMapping("/ownership/transfer/{serverId}/{currentOwner}/{userId}")
	private ResponseEntity<ResponseModel<String>> transferOwnership(@PathVariable String serverId, @PathVariable String currentOwner, @PathVariable String userId) {
		ResponseModel<String> body = new ResponseModel<String>("Successfully transfered ownership.", true);
		HttpStatus status = HttpStatus.OK;
		try {
			accessService.transferServerOwnership(serverId, currentOwner, userId);
		} catch (AccessException e) { 
			e.printStackTrace();
			body = new ResponseModel<String>("Access denied only owner can transfer ownership.", false);
			body.setError(e.getMessage());
			status = HttpStatus.FORBIDDEN;
		}catch (Exception e) {
			e.printStackTrace();
			body = new ResponseModel<String>("Something went wrong!", false);
			body.setError(e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}		
		return ResponseEntity.status(status).body(body);
	}
	
	@GetMapping("/provide/access/{serverId}/{currentOwner}/{userId}")
	private ResponseEntity<ResponseModel<String>> giveAccess(@PathVariable String serverId, @PathVariable String currentOwner, @PathVariable String userId) {
		ResponseModel<String> body = new ResponseModel<String>("Successfully provided acess.", true);
		HttpStatus status = HttpStatus.OK;
		try {
			accessService.provideAccessToServer(serverId, currentOwner, userId);
		} catch (AccessException e) { 
			e.printStackTrace();
			body = new ResponseModel<String>("Access denied only owner can provide access.", false);
			body.setError(e.getMessage());
			status = HttpStatus.FORBIDDEN;
		}catch (Exception e) {
			e.printStackTrace();
			body = new ResponseModel<String>("Something went wrong!", false);
			body.setError(e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}		
		return ResponseEntity.status(status).body(body);
	}
	
	@GetMapping("/revoke/access/{serverId}/{currentOwner}/{userId}")
	private ResponseEntity<ResponseModel<String>> revokeAccess(@PathVariable String serverId, @PathVariable String currentOwner, @PathVariable String userId) {
		ResponseModel<String> body = new ResponseModel<String>("Successfully revoked access.", true);
		HttpStatus status = HttpStatus.OK;
		try {
			accessService.revokeAccessToServer(serverId, currentOwner, userId);
		} catch (AccessException e) { 
			e.printStackTrace();
			body = new ResponseModel<String>("Access denied only owner can revoke access.", false);
			body.setError(e.getMessage());
			status = HttpStatus.FORBIDDEN;
		}catch (Exception e) {
			e.printStackTrace();
			body = new ResponseModel<String>("Something went wrong!", false);
			body.setError(e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}		
		return ResponseEntity.status(status).body(body);
	}

}
