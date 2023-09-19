package gmc.project.infrasight.presentationservice.entities.embedded;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class DiscMountEntity implements Serializable {

	private static final long serialVersionUID = -5454181583827669670L;
	
	public DiscMountEntity() {}
	
	public DiscMountEntity(String line) {
		String[] words = line.replaceAll("\\s{2,}", " ").split(" ");
		this.fileSystem = words[0];
		this.size = words[1];
		this.used = words[2];
		this.available = words[3];
		this.use = words[4];
		this.mountedOn = words[5];
	}
	
	@Id
	private String id;

	private String fileSystem;

	private String size;

	private String used;

	private String available;

	private String use;

	private String mountedOn;

}
