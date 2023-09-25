package gmc.project.infrasight.statscaptureservice.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class MailingModel implements Serializable {

	private static final long serialVersionUID = 2094682740789699002L;

	private String to;

	private String subject;

	private String body;

}
