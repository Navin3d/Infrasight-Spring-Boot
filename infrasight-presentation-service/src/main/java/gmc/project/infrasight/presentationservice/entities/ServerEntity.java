package gmc.project.infrasight.presentationservice.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import gmc.project.infrasight.presentationservice.entities.embedded.DiscStatsEntity;
import gmc.project.infrasight.presentationservice.entities.embedded.IOStatEntity;
import gmc.project.infrasight.presentationservice.entities.embedded.StatsEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(exclude = { "projects", "serverUsers", "tasks" })
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

	private String serverUpTime;
	
	private Boolean isActive;
	
	private Double cpuLimit;
	
	private Long ramLimit;
	
	private LocalDate lastRamNotificationSent;
	
	private LocalDate lastCpuNotificationSent;
	
	private LocalDate lastDownNotificationSent;
		
	private Set<StatsEntity> ramCPU = new HashSet<>();
	
	private Set<DiscStatsEntity> discStats = new HashSet<>();
	
	private Set<IOStatEntity> ioStats = new HashSet<>();
	
	@DBRef
	private UserEntity serverAdmin;

	@DBRef
	private Set<UserEntity> serverUsers = new HashSet<>();
	
	@DBRef
	private Set<ProjectEntity> projects = new HashSet<>();
	
	@DBRef
	private Set<TaskEntity> tasks = new HashSet<>();

}
