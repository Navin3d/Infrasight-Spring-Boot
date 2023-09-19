package gmc.project.infrasight.presentationservice.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ResponseModel<T> implements Serializable {
	
	private static final long serialVersionUID = 618522115541075774L;
	
	private String message;
	
	private Boolean status;
	
	private String error;
	
	private T data;
	
	private List<T> datas = new ArrayList<>();
	
	public ResponseModel(String message) {
		this.message = message;
		this.status = true;
	}

}
