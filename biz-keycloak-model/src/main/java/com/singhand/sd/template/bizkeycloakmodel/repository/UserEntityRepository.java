package com.singhand.sd.template.bizkeycloakmodel.repository;

import com.singhand.sd.template.bizkeycloakmodel.model.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, String>,
    JpaSpecificationExecutor<UserEntity> {

  boolean existsByUsername(String username);

  Optional<UserEntity> findByUsername(String username);

  Optional<UserEntity> findByUsernameAndRealmId(String username, String realmId);
}