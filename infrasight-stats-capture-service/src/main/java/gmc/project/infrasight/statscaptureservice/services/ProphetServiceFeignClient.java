package gmc.project.infrasight.statscaptureservice.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import gmc.project.infrasight.statscaptureservice.models.MailingModel;


@FeignClient("Infrasight-Prophet-Service")
public interface ProphetServiceFeignClient {
	
	@PostMapping(path = "/mail")
	public ResponseEntity<String> sendMail(@RequestBody MailingModel mailingModel);

}
