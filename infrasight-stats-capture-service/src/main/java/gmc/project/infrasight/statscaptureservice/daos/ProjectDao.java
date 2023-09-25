package gmc.project.infrasight.statscaptureservice.daos;

import org.springframework.data.mongodb.repository.MongoRepository;

import gmc.project.infrasight.statscaptureservice.entities.ProjectEntity;

public interface ProjectDao extends MongoRepository<ProjectEntity, String> {

}
