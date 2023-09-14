package gmc.project.infrasight.serverservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@EnableDiscoveryClient
@SpringBootApplication
public class InfrasightServerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InfrasightServerServiceApplication.class, args);
	}

}
