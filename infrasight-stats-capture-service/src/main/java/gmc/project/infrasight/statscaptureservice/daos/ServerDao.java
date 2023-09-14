package gmc.project.infrasight.statscaptureservice.daos;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import gmc.project.infrasight.statscaptureservice.entities.ServerEntity;

public interface ServerDao extends MongoRepository<ServerEntity, String> {
	public Optional<ServerEntity> findByHost(String host);
}
