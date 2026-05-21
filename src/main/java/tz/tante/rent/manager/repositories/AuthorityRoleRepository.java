package tz.tante.rent.manager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tz.tante.rent.manager.enums.AuthorityRoleName;
import tz.tante.rent.manager.models.entities.AuthorityRole;

public interface AuthorityRoleRepository extends JpaRepository<AuthorityRole, Long>
{
  boolean existsByName(AuthorityRoleName authorityRoleName);

  AuthorityRole findByName(AuthorityRoleName name);
}
