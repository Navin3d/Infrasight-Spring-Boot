package gmc.project.infrasight.serverservice.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class CreateServerModel implements Serializable {

	private static final long serialVersionUID = -3088393190082110370L;

	private String name;

	private String description;

	private String host;

	private Integer port;

	private String username;

	private String password;

}
