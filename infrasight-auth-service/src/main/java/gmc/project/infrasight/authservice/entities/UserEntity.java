package gmc.project.infrasight.authservice.entities;

import java.io.Serializable;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

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

}
