package tz.tante.property.manager.models.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "buildings")
@NoArgsConstructor
public class Building extends BaseEntity
{
  private String name;
  private int number;
  private String code;
  private String description;
  private int numberOfFloors;
  private int numberOfUnits;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "property_id")
  private Property property;

  @OneToMany(
    mappedBy = "building",
    cascade = CascadeType.ALL,
    fetch = FetchType.LAZY
  )
  private List<Unit> units = new ArrayList<>();

}
