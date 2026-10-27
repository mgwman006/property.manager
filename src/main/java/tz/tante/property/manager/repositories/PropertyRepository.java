package tz.tante.property.manager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.tante.property.manager.models.entities.Property;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long>
{
  List<Property> findByRentalProfileId(Long rentalProfileId);
}
