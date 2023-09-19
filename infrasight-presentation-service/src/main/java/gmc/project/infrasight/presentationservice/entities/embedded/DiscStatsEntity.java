package gmc.project.infrasight.presentationservice.entities.embedded;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class DiscStatsEntity implements Serializable {
	
	private static final long serialVersionUID = -2835692209855993705L;
	
	@Id
	private String id;
	
	private Set<DiscMountEntity> discMounts = new HashSet<>();
		
	private LocalDate capturedAt;
	
	public DiscStatsEntity() {
		this.capturedAt = LocalDate.now();
	}

}
