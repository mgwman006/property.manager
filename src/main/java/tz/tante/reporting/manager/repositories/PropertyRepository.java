package tz.tante.reporting.manager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tz.tante.reporting.manager.models.entities.Property;

public interface PropertyRepository extends JpaRepository<Property, Long>
{
}
