package gmc.project.infrasight.statscaptureservice.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("Infrasight-ML-Service")
public interface MLServiceFeignClient {
	@GetMapping(path = "/api/scale_trigger/{cpu}/{disc}/{ram}/{load}/{uptime}/")
	public String suggestScaling(@PathVariable Integer cpu, @PathVariable Integer disc, @PathVariable Integer ram, @PathVariable Integer load, @PathVariable Integer uptime);
}
