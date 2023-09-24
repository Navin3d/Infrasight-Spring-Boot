package gmc.project.infrasight.prophetservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gmc.project.infrasight.prophetservice.services.MessageService;

@RestController
@RequestMapping(path = "/sms")
public class SmsController {

	@Autowired
	private MessageService messageService;
	
	@GetMapping(value = "/sendSMS/{mobileNumber}/{message}")
	public ResponseEntity<String> sendSMS(@PathVariable String mobileNumber, @PathVariable String message) {
		messageService.sendMessage(mobileNumber, message);
		return new ResponseEntity<String>("Message sent successfully", HttpStatus.OK);
	}

}
