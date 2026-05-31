package tz.tante.reporting.manager.models.entities;

import tz.tante.reporting.manager.enums.UnitStatus;
import tz.tante.reporting.manager.enums.UnitType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "units")
@Table(name = "units")
public class Unit extends BaseEntity
{
  private String unitNumber;

  private BigDecimal rentAmount;

  @Enumerated(EnumType.STRING)
  private UnitType type;

  @Enumerated(EnumType.STRING)
  private UnitStatus status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "property_id")
  private Property property;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "rental_profile_id")
  private RentalProfile rentalProfile;

  @OneToMany(mappedBy = "unit", fetch = FetchType.LAZY)
  private List<Site> sites = new ArrayList<>();
}
