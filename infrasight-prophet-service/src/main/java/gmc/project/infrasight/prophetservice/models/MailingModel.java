package gmc.project.infrasight.prophetservice.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class MailingModel implements Serializable {
	
	private static final long serialVersionUID = -8389188671432935946L;
	
	private String to;
	
	private String subject;
	
	private String body;

}
