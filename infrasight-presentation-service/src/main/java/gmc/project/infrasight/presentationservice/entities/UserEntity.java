package gmc.project.infrasight.presentationservice.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(exclude = { "adminOfServers", "accessibleServers" })
@Data
@Document(collection = "users")
public class UserEntity implements Serializable {
	
	private static final long serialVersionUID = 8312692146136568286L;
	
	@Id
	private String id;
	
	private String name;
	
	private Integer employeeId;
	
	private String companyEmail;
	
	private Binary profilePic;
	
	private String username;
	
	private String password;
	
	@DBRef
	private Set<ServerEntity> adminOfServers = new HashSet<>();
	
	@DBRef
	private Set<ServerEntity> accessibleServers = new HashSet<>();

}