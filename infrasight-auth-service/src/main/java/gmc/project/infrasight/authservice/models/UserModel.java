package gmc.project.infrasight.authservice.models;

import java.io.Serializable;

import org.bson.types.Binary;

import lombok.Data;

@Data
public class UserModel implements Serializable {

	private static final long serialVersionUID = 8045516547402966238L;

	private String id;

	private String name;

	private Integer employeeId;

	private String companyEmail;

	private String username;
	
	private String passwordPlain;
	
	private Binary profilePic;
	
}
