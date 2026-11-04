package tz.tante.property.manager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.tante.property.manager.models.entities.UnitImage;

import java.util.List;

@Repository
public interface UnitImageRepository extends JpaRepository<UnitImage, Long>
{
  List<UnitImage> findByUnitIdOrderByCreatedAtAsc(Long unitId);
}
