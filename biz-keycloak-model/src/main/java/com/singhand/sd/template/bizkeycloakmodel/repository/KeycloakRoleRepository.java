package com.singhand.sd.template.bizkeycloakmodel.repository;

import com.singhand.sd.template.bizkeycloakmodel.model.KeycloakRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeycloakRoleRepository extends JpaRepository<KeycloakRole, String> {

  boolean existsByNameAndClientRole(String roleName, Boolean clientRole);
}