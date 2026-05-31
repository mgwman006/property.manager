package tz.tante.reporting.manager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tz.tante.reporting.manager.enums.AuthorityRoleName;
import tz.tante.reporting.manager.models.entities.AuthorityRole;

public interface AuthorityRoleRepository extends JpaRepository<AuthorityRole, Long>
{
  boolean existsByName(AuthorityRoleName authorityRoleName);

  AuthorityRole findByName(AuthorityRoleName name);
}
