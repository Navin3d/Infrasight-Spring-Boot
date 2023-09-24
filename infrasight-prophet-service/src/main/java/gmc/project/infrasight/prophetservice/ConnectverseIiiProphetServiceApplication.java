package gmc.project.infrasight.prophetservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class ConnectverseIiiProphetServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConnectverseIiiProphetServiceApplication.class, args);
	}

}
