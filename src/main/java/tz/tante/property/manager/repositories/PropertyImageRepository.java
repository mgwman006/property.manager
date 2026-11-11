package tz.tante.property.manager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tz.tante.property.manager.models.entities.PropertyImage;

import java.util.List;

public interface PropertyImageRepository extends JpaRepository<PropertyImage, Long>
{
  List<PropertyImage> findByPropertyIdOrderByCreatedAtAsc(Long propertyId);
}
