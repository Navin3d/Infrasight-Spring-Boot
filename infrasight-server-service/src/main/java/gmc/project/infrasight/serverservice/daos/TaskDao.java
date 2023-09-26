package gmc.project.infrasight.serverservice.daos;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import gmc.project.infrasight.serverservice.entities.TaskEntity;

public interface TaskDao extends MongoRepository<TaskEntity, String> {
	public List<TaskEntity> findByAtEndOfDay(Boolean atEndOfDay);
}
