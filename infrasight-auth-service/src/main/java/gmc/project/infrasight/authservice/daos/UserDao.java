package gmc.project.infrasight.authservice.daos;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import gmc.project.infrasight.authservice.entities.UserEntity;

public interface UserDao extends MongoRepository<UserEntity, String> {
	Optional<UserEntity> findByUsername(String username);
	Optional<UserEntity> findByEmployeeId(Integer employeeId);
	Optional<UserEntity> findByCompanyEmail(String companyEmail);
}
