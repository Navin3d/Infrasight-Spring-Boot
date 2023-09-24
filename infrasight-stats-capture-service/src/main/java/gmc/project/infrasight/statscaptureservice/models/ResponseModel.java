package gmc.project.infrasight.statscaptureservice.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ResponseModel<T> implements Serializable {
	
	private static final long serialVersionUID = -4541142459145835552L;

	private String message;
	
	private Boolean status;
	
	private String error;
	
	private T data;
	
	private List<T> datas = new ArrayList<>();
	
	public ResponseModel(String message, Boolean status) {
		this.message = message;
		this.status = status;
	}

}
