package gmc.project.infrasight.statscaptureservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("encryption")
public class EncryptionConfig {
	
    private String secretKey;

    private String algorithm;

}
