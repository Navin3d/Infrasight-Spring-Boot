package gmc.project.infrasight.serverservice.entities.embedded;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class IOStatEntity implements Serializable {
    
    private static final long serialVersionUID = -2835692209855993705L;
	
	@Id
	private String id;
	
	private Set<IOStatData> ioDatas = new HashSet<>();
		
	private LocalDate capturedAt;
	
	public IOStatEntity() {
		this.capturedAt = LocalDate.now();
	}
    
}
