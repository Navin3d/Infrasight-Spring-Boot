package gmc.project.infrasight.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@EnableMongoRepositories
@EnableDiscoveryClient
@SpringBootApplication
public class InfrasightAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InfrasightAuthServiceApplication.class, args);
	}

}
