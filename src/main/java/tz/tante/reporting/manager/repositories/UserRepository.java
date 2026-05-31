package tz.tante.reporting.manager.repositories;

import tz.tante.reporting.manager.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long>
{
}
