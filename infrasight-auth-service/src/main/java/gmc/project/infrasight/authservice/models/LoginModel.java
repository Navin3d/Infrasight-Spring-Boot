package gmc.project.infrasight.authservice.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class LoginModel implements Serializable {
	
	private static final long serialVersionUID = -2542743750112603124L;
	
	private String userName;
	
	private String password;

}
