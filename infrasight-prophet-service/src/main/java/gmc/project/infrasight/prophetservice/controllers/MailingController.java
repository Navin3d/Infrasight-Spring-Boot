package gmc.project.infrasight.prophetservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gmc.project.infrasight.prophetservice.models.MailingModel;
import gmc.project.infrasight.prophetservice.services.MailingService;

@RestController
@RequestMapping(path = "/mail")
public class MailingController {
	
	@Autowired
	private MailingService mailingService;
	
	@PostMapping
	public ResponseEntity<String> sendMail(@RequestBody MailingModel mailingModel) {
		mailingService.sendMail(mailingModel);
		return ResponseEntity.status(HttpStatus.OK).body("Mail Sent!.");
	}

}
