package gmc.project.infrasight.statscaptureservice.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import gmc.project.infrasight.statscaptureservice.entities.embedded.StatsEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(exclude = { "ramCpuStats" })
@Data
@Document(collection = "projects")
public class ProjectEntity implements Serializable {
	
	private static final long serialVersionUID = -6664442700356289433L;
	
	@Id
	private String id;
		
	private String programmingLanguage;
	
	private String framework;
	
	@DBRef
	private ServerEntity installedOn;
	
	private Set<StatsEntity> ramCpuStats = new HashSet<>();
	
//	public ProjectEntity(String projectLine) {
//		log.error(projectLine);
//		Set<StatsEntity> ramCpuStats = new HashSet<>();
//		List<String> ids = new ArrayList<>();
//		String[] lines = projectLine.split("\n");
//		for(String line : lines) {
//			String[] words = line.split("\\s+");
//			log.error("CPU: {} RAM: {} Project: {}", words[2], words[3], words[11]);
//			StatsEntity statsEntity = new StatsEntity();
//			statsEntity.setCpuPerformance(Double.valueOf(words[2]));
//			statsEntity.setRamPerformance(Double.valueOf(words[3]));
//			String regex = ".*\\/([^\\/]+)\\/node_modules\\/(.*)?";
//			Pattern pattern = Pattern.compile(regex);
//			Matcher matcher = pattern.matcher(words[11]);
//			if (matcher.matches()) {
//			    String result = matcher.group(1);
//			    statsEntity.setId(result);
//			    ids.add(result);
//			} else {
//				statsEntity.setId("UNKNOWN");
//				ids.add("UNKNOWN");
//			}
//			if(!ids.contains(statsEntity.getId())) {
//				ids.add(statsEntity.getId());
//				ramCpuStats.add(statsEntity);
//			}
//		}
//		this.ramCpuStats = ramCpuStats;
//	}

}
