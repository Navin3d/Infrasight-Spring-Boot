package gmc.project.infrasight.statscaptureservice.entities.embedded;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class IOStatData implements Serializable {

    private static final long serialVersionUID = -5454181583827669670L;
	
	public IOStatData() {}
	
	public IOStatData(String line) {
		String[] words = line.replaceAll("\\s+", " ").split(" ");
		this.device = words[0];
		this.transferPerSecond = words[1];
		this.readPerSecond = words[2];
		this.writePerSecond = words[3];
		this.averageRead = words[5];
		this.averageWrite = words[6];
	}
	
	@Id
	private String id;

	private String device;

	private String transferPerSecond;

	private String readPerSecond;

	private String writePerSecond;

	private String averageRead;

	private String averageWrite;
    
}
