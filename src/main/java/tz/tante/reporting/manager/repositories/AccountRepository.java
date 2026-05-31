package tz.tante.reporting.manager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.tante.reporting.manager.models.entities.Project;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Project, Long>
{
  Optional<Project> findByEmail(String email);
  Optional<Project> findByEmailAndPassword(String email, String password);
  Optional<Project> findByPhoneNumber(String phoneNumber);
  boolean existsByPhoneNumber(String phoneNumber);
}
