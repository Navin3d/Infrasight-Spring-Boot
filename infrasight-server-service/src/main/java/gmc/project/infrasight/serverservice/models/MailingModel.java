package gmc.project.infrasight.serverservice.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class MailingModel implements Serializable {
	
	private static final long serialVersionUID = 5025085034812041435L;
	
	private String to;

	private String subject;

	private String body;

}
