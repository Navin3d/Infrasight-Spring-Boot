package gmc.project.infrasight.statscaptureservice.concurreny;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemoryCPUStringModel implements Serializable {
	
	private static final long serialVersionUID = 1641295584477346936L;
	
	public MemoryCPUStringModel() {}
	
	public MemoryCPUStringModel(List<String> ramResponseLines, List<String> cpuResponseLines,
			List<String> swapResponseLines, List<String> loadResponseLine, List<String> projectResponseLine) {
		super();
		this.ramResponseLines = ramResponseLines;
		this.cpuResponseLines = cpuResponseLines;
		this.swapResponseLines = swapResponseLines;
		this.loadResponseLine = loadResponseLine;
		this.projectResponseLine = projectResponseLine;
	}
	
	private List<String> ramResponseLines = new ArrayList<>();
	private List<String> cpuResponseLines = new ArrayList<>();
	private List<String> swapResponseLines = new ArrayList<>();
	private List<String> loadResponseLine = new ArrayList<>();
	private List<String> projectResponseLine = new ArrayList<>();

}
