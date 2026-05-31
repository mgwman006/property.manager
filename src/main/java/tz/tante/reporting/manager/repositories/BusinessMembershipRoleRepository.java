package tz.tante.reporting.manager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tz.tante.reporting.manager.enums.BusinessMembershipRoleName;
import tz.tante.reporting.manager.models.entities.BusinessMembershipRole;

import java.util.Optional;

public interface BusinessMembershipRoleRepository extends JpaRepository<BusinessMembershipRole, Long>
{
  Optional<BusinessMembershipRole> findByName(BusinessMembershipRoleName name);
  boolean existsByName(BusinessMembershipRoleName name);
}