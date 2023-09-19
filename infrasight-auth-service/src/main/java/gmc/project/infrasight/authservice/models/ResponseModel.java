package gmc.project.infrasight.authservice.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ResponseModel implements Serializable {
	
	private static final long serialVersionUID = -2460399143574271781L;
	
	public ResponseModel (String message, Boolean status) {
		this.message = message;
		this.status = status;
	}

	private String message;
	
	private Boolean status;
	
	private String error;
	
	private Object data;
	
	private List<Object> datas = new ArrayList<>();
	
}
