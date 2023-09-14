package gmc.project.infrasight.serverservice.daos;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import gmc.project.infrasight.serverservice.entities.ServerEntity;

public interface ServerDao extends MongoRepository<ServerEntity, String> {
	public Optional<ServerEntity> findByHost(String host);
}
