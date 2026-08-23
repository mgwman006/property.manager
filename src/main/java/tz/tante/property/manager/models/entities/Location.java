package tz.tante.property.manager.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "property_locations")
public class Location extends BaseEntity
{
  @Column(nullable = false)
  private Double latitude;

  @Column(nullable = false)
  private Double longitude;

  @Embedded
  private Address address;

  @OneToOne(fetch = FetchType.LAZY)
  private Property property;
}
