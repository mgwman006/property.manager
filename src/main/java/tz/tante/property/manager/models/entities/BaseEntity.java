package tz.tante.property.manager.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Version
  private Long version;

  @Column(updatable = false)
  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private Long createdByUserId;

  private Long updatedByUserId;

  private boolean isDeleted = false;

  @PrePersist
  protected void onCreate()
  {
    LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
    createdAt = now;
    updatedAt = now;
  }

  @PreUpdate
  protected void onUpdate()
  {
    updatedAt = LocalDateTime.now(ZoneId.of("UTC"));
  }
}