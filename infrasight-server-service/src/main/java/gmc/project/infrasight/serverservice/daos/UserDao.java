package gmc.project.infrasight.serverservice.daos;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import gmc.project.infrasight.serverservice.entities.UserEntity;


public interface UserDao extends MongoRepository<UserEntity, String> {
	public Optional<UserEntity> findByUsername(String username);
	public Optional<UserEntity> findByCompanyEmail(String companyEmail);
	public Optional<UserEntity> findByEmployeeId(Integer employeeId);
}
