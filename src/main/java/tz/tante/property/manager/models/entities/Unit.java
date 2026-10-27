package tz.tante.property.manager.models.entities;




import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import tz.tante.property.manager.enums.UnitStatus;
import tz.tante.property.manager.enums.UnitType;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(
  name = "units",
  uniqueConstraints = {
    @UniqueConstraint(
      name = "uk_unit_building_unit_number",
      columnNames = {"building_id", "unit_number"}
    )
  }
)
public class Unit extends BaseEntity
{
  private String unitNumber;

  private Long rentalProfileId;

  private int numberOfBedrooms;

  private int numberOfBathrooms;

  private int numberParkingSpots;

  private BigDecimal rentAmount;

  @Enumerated(EnumType.STRING)
  private UnitType type;

  @Enumerated(EnumType.STRING)
  private UnitStatus status;

  private Long roomSize;

  private  String sizeUnit;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "building_id")
  private Building building;
}
