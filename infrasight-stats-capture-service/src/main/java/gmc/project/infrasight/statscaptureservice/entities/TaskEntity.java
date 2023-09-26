package gmc.project.infrasight.statscaptureservice.entities;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "tasks")
public class TaskEntity implements Serializable {
	
	private static final long serialVersionUID = 9101273753685724547L;
	
	@Id
	private String id;
	
	private String tittle;
	
	private String command;
	
	private Boolean atEndOfDay;
	
	private Boolean deleteAfterExecution;
	
	private ServerEntity runOnServers;

}

