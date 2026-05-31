package tz.tante.reporting.manager.models.entities;

import jakarta.persistence.*;
import lombok.*;
import tz.tante.reporting.manager.enums.ApplicationStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rental_applications")
public class RentalApplication {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // =========================
  // TARGET UNIT (what they want)
  // =========================
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "unit_id", nullable = false)
  private Unit unit;

  // =========================
  // APPLICANT (NOT A TENANT YET)
  // =========================
  private String applicantFullName;

  private String applicantPhone;

  private String applicantNationalId;

  private String applicantEmail;

  // =========================
  // APPLICATION DETAILS
  // =========================
  private BigDecimal offeredRent;

  private BigDecimal depositOffered;

  private String message;

  // =========================
  // STATUS
  // =========================
  @Enumerated(EnumType.STRING)
  private ApplicationStatus status;

  private String rejectionReason;

  // =========================
  // TIMELINE
  // =========================
  private LocalDateTime appliedAt;

  private LocalDateTime processedAt;

  private LocalDateTime expiresAt;

  // =========================
  // LIFECYCLE
  // =========================
  @PrePersist
  public void onCreate() {
    this.appliedAt = LocalDateTime.now();
    this.status = ApplicationStatus.PENDING;
  }
}