package tz.tante.property.manager.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.tante.property.manager.enums.PropertyOwnerType;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "property_owners")
@AllArgsConstructor
@NoArgsConstructor
public class PropertyOwner extends BaseEntity
{
  @Enumerated(EnumType.STRING)
  private PropertyOwnerType ownerType;

  @Column(nullable = false)
  private Long ownerId;

  @Column(nullable = false)
  private BigDecimal ownershipPercentage;

  @ManyToOne(fetch = FetchType.LAZY)
  private Property property;
}
