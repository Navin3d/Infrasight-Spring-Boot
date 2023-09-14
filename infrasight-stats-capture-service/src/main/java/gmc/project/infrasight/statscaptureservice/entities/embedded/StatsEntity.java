package gmc.project.infrasight.statscaptureservice.entities.embedded;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class StatsEntity implements Serializable {
	
	private static final long serialVersionUID = -618501582385421256L;
	
	@Id
	private String id;
	
	private Long availableRam;
	
	private Long totalRam;
	
	private Double cpuPerformance;
	
	private Boolean isActive;
	
	private LocalDateTime capturedAt;
		
	public Long usedRam() {
		return this.totalRam - this.availableRam;
	}
	
	public StatsEntity(Boolean isActive) {
		this.isActive = isActive;
		this.capturedAt = LocalDateTime.now();
	}

}
