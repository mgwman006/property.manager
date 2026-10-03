package tz.tante.property.manager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.tante.property.manager.models.entities.Building;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long>
{
}
