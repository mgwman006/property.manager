package tz.tante.reporting.manager.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tz.tante.reporting.manager.enums.PaymentStatus;
import tz.tante.reporting.manager.enums.PaymentType;
import tz.tante.reporting.manager.enums.PaymentMethod;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "payments")
public class Payment extends BaseEntity
{
  @Column(nullable = false)
  private String referenceId = UUID.randomUUID().toString();

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal amount;

  private LocalDate paymentDate;

  @Enumerated(EnumType.STRING)
  private PaymentStatus status;

  @Enumerated(EnumType.STRING)
  private PaymentType type;

  @Enumerated(EnumType.STRING)
  private PaymentMethod method;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lease_id")
  private Site site;
}