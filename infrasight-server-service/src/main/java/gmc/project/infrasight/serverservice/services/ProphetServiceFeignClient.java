package gmc.project.infrasight.serverservice.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import gmc.project.infrasight.serverservice.models.MailingModel;


@FeignClient("Infrasight-Prophet-Service")
public interface ProphetServiceFeignClient {
	
	@PostMapping(path = "/mail")
	public ResponseEntity<String> sendMail(@RequestBody MailingModel mailingModel);

}

