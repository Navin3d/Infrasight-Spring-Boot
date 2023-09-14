package gmc.project.infrasight.discoveryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class InfrasightDiscoveryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InfrasightDiscoveryServiceApplication.class, args);
	}

}
