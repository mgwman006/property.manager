package tz.tante.property.manager.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import tz.tante.property.manager.enums.DevelopmentStatus;
import tz.tante.property.manager.enums.PropertyCategory;
import tz.tante.property.manager.enums.PropertyStatus;

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

  private Long managingOrganizationId;

  @Column(nullable = false)
  private Long creatorId;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String code;

  private Long landSize;

  private String landSizeUnit;

  @Column(nullable = false)
  private Double latitude;

  @Column(nullable = false)
  private Double longitude;

  @Embedded
  private Address address;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private PropertyCategory type;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private DevelopmentStatus developmentStatus;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private PropertyStatus status;

  @OneToMany(
    mappedBy = "property",
    cascade = CascadeType.ALL,
    fetch = FetchType.LAZY
  )
  private List<Unit> units = new ArrayList<>();

  public Property(String description, Long aLong, Long aLong1, String name,
                  String s, Long aLong2, String s1, Double latitude, Double longitude,
                  Address address, PropertyCategory type, DevelopmentStatus developmentStatus, PropertyStatus status)
  {
    this.description = description;
    this.managingOrganizationId = aLong;
    this.creatorId = aLong1;
    this.name = name;
    this.code = s;
    this.landSize = aLong2;
    this.landSizeUnit = s1;
    this.latitude = latitude;
    this.longitude = longitude;
    this.address = address;
    this.type = type;
    this.developmentStatus = developmentStatus;
    this.status = status;
  }
}
