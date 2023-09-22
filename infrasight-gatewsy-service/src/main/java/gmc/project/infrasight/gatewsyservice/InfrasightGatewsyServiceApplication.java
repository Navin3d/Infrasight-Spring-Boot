package gmc.project.infrasight.gatewsyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class InfrasightGatewsyServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InfrasightGatewsyServiceApplication.class, args);
	}

}
