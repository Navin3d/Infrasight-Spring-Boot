package gmc.project.infrasight.statscaptureservice.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class CommandModel implements Serializable {
	
	private static final long serialVersionUID = 2798277671066524856L;
	
	private String serverId;
	private String command;

}
