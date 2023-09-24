package gmc.project.infrasight.serverservice.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ResponseModel<T> implements Serializable {
	
	private static final long serialVersionUID = 2316799527301927550L;
	
	public ResponseModel(String message, Boolean status) {
		this.message = message;
		this.status = status;
	}
	
	private String message;
	
	private Boolean status;
		
	private T data;
	
	private List<T> datas = new ArrayList<>();
	
	private String error;

}
