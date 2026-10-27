package tz.tante.property.manager.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import tz.tante.property.manager.enums.DevelopmentStatus;
import tz.tante.property.manager.enums.PropertyRegister;
import tz.tante.property.manager.enums.PropertyType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "properties")
public class Property extends BaseEntity
{
  private Long rentalProfileId;

  @Column(length = 2000)
  private String description;

  private String name;

  @Column(nullable = false, unique = true)
  private String code;

  private Double landSize;

  private String landSizeUnit;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private PropertyRegister registeredBy;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "property")
  private Location location;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private PropertyType type;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private DevelopmentStatus developmentStatus;

  @OneToMany(
    mappedBy = "property",
    cascade = CascadeType.ALL,
    fetch = FetchType.LAZY
  )
  private List<Building> buildings = new ArrayList<>();

  @OneToMany(
    mappedBy = "property",
    cascade = CascadeType.ALL,
    fetch = FetchType.LAZY
  )
  private List<PropertyOwner> owners = new ArrayList<>();

  public void addBuilding(Building building) {
    buildings.add(building);
    building.setProperty(this);
  }

}
