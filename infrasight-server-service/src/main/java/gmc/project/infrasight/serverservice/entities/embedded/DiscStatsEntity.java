package gmc.project.infrasight.serverservice.entities.embedded;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class DiscStatsEntity implements Serializable {
	
	private static final long serialVersionUID = -2835692209855993705L;
	
	@Id
	private String id;
	
	private String fileSystem;
	
	private String size;
	
	private String used;
	
	private String available;
	
	private String use;
	
	private String mountedOn;
		
	private LocalDate capturedAt;

}
