package tz.tante.reporting.manager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tz.tante.reporting.manager.models.entities.Site;

public interface LeaseRepository extends JpaRepository<Site, Long>
{
}
