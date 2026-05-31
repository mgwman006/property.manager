package tz.tante.reporting.manager.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tz.tante.reporting.manager.enums.SiteStatus;


@Getter
@Setter
@Entity
@Table(name = "sites")
public class Site extends BaseEntity
{
  private String name;

  private String description;

  @Column(nullable = false)
  private Long propertyId;

  @Enumerated(EnumType.STRING)
  private SiteStatus siteStatus;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id", nullable = false)
  private Project project;

}
