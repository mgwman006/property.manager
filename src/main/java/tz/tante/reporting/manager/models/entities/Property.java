package tz.tante.reporting.manager.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tz.tante.reporting.manager.enums.OwnershipType;
import tz.tante.reporting.manager.enums.PropertyType;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "properties")
public class Property extends BaseEntity
{
  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private PropertyType type;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OwnershipType ownershipType;

  @OneToMany(
    mappedBy = "property",
    cascade = CascadeType.ALL,
    fetch = FetchType.LAZY
  )
  private List<Unit> units = new ArrayList<>();


  @Embedded
  private Address address;

  @Column(nullable = false)
  private Double latitude;

  @Column(nullable = false)
  private Double longitude;
}
