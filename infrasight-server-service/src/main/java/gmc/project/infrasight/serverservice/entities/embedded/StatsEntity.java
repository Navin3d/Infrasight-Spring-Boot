package gmc.project.infrasight.serverservice.entities.embedded;

import java.io.Serializable;

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
	
	public Long usedRam() {
		return this.totalRam - this.availableRam;
	}

}
