package gmc.project.infrasight.statscaptureservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "app")
public class AppConfig {
	
	private Integer totalThreadLimit;

}
