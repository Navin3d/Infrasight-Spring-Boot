package gmc.project.infrasight.presentationservice.daos;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import gmc.project.infrasight.presentationservice.entities.ProjectEntity;

public interface ProjectDao extends MongoRepository<ProjectEntity, String> {
	public List<ProjectEntity> findByProgrammingLanguage(String programmingLanguage);
	public List<ProjectEntity> findByFramework(String framework);
	
	public List<ProjectEntity> findByProgrammingLanguage(String programmingLanguage, Pageable pageable);
	public List<ProjectEntity> findByFramework(String framework, Pageable pageable);
}
