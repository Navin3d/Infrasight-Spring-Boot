package gmc.project.infrasight.statscaptureservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@EnableScheduling
@EnableMongoRepositories
@EnableDiscoveryClient
@SpringBootApplication
public class InfrasightStatsCaptureServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InfrasightStatsCaptureServiceApplication.class, args);
	}

}
