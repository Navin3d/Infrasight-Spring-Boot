package gmc.project.infrasight.presentationservice.entities;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "projects")
public class ProjectEntity implements Serializable {
	
	private static final long serialVersionUID = -6664442700356289433L;
	
	@Id
	private String id;
		
	private String tittle;
	
	private String description;
	
	private String programmingLanguage;
	
	private String framework;
	
	@DBRef
	private ServerEntity installedOn;

}
