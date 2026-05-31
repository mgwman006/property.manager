package tz.tante.reporting.manager.repositories;

import tz.tante.reporting.manager.models.entities.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepository extends JpaRepository<Tenant,Long> {

}
