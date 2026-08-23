package tz.tante.property.manager.repositories;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tz.tante.property.manager.models.entities.PropertySequence;

import java.util.Optional;

public interface PropertySequenceRepository extends JpaRepository<PropertySequence, Long>
{
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT s FROM PropertySequence s WHERE s.year = :year")
  Optional<PropertySequence> findForUpdate(@Param("year") int year);
}
