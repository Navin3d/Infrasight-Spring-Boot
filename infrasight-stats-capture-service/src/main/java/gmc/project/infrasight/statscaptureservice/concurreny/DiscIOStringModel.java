package gmc.project.infrasight.statscaptureservice.concurreny;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiscIOStringModel implements Serializable {
	
	private static final long serialVersionUID = 1641295584477346936L;
	
	public DiscIOStringModel() {}
	
	public DiscIOStringModel(List<String> discResponseLines, List<String> ioResponseLines) {
		super();
		this.discResponseLines = discResponseLines;
		this.ioResponseLines = ioResponseLines;
	}
	
	private List<String> discResponseLines = new ArrayList<>();
	private List<String> ioResponseLines = new ArrayList<>();

}
