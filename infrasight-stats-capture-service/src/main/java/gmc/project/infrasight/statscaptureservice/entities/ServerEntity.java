package gmc.project.infrasight.statscaptureservice.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import gmc.project.infrasight.statscaptureservice.entities.embedded.DiscStatsEntity;
import gmc.project.infrasight.statscaptureservice.entities.embedded.StatsEntity;
import lombok.Data;

@Data
@Document(collection = "servers")
public class ServerEntity implements Serializable {
	
	private static final long serialVersionUID = 7449493827033800191L;
	
	@Id
	private String id;
	
	private String name;
	
	private String description;
	
	private String host;
	
	private Integer port;
	
	private String username;
	
	private String password;
	
	private Boolean isActive;
		
	private Set<StatsEntity> ramCPU = new HashSet<>();
	
	private Set<DiscStatsEntity> discStats = new HashSet<>();
	
	@DBRef
	private Set<ProjectEntity> projects = new HashSet<>();

}
