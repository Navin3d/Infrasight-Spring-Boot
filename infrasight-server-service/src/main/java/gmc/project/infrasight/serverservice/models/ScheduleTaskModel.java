package gmc.project.infrasight.serverservice.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class ScheduleTaskModel implements Serializable {
	
	private static final long serialVersionUID = -2842486555456797892L;
	
	private String id;
	
	private String serverId;
	
	private String tittle;
	
	private String command;
	
	private Boolean atEndOfDay;
	
	private Boolean deleteAfterExecution;

}
