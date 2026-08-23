package tz.tante.property.manager.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import tz.tante.property.manager.enums.DevelopmentStatus;
import tz.tante.property.manager.enums.PropertyCategory;

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
  @Column(length = 2000)
  private String description;

  private String name;

  @Column(nullable = false, unique = true)
  private String code;

  private Double landSize;

  private String landSizeUnit;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "property")
  private Location location;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private PropertyCategory type;

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

}
