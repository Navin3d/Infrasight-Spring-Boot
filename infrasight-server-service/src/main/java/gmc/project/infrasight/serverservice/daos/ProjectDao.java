package gmc.project.infrasight.serverservice.daos;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import gmc.project.infrasight.serverservice.entities.ProjectEntity;

public interface ProjectDao extends MongoRepository<ProjectEntity, String> {
	public List<ProjectEntity> findByProgrammingLanguage(String programmingLanguage);
	public List<ProjectEntity> findByFramework(String framework);
}
