package tz.tante.property.manager.models.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
  name = "unit_listing_managers",
  uniqueConstraints = {
    @UniqueConstraint(
      name = "uk_unit_listing_manager",
      columnNames = {"unit_id", "rental_profile_id"}
    )
  }
)
public class UnitListingManager extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "unit_id", nullable = false)
  private Unit unit;

  @Column(name = "rental_profile_id", nullable = false)
  private Long rentalProfileId;
}
