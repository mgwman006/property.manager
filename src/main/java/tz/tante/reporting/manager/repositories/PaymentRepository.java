package tz.tante.reporting.manager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tz.tante.reporting.manager.models.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>
{
}
