package tz.tante.reporting.manager.repositories;

import tz.tante.reporting.manager.models.entities.RentalProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LandlordRepository extends JpaRepository<RentalProfile,Long>
{
}
