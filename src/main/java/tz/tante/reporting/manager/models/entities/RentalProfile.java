package tz.tante.reporting.manager.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.tante.reporting.manager.enums.RentalProfileType;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (name = "rental_profiles")
public class RentalProfile extends BaseEntity
{
  private String name;

  private String businessEmail;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RentalProfileType type;

  @OneToMany(mappedBy = "rentalProfile", fetch = FetchType.LAZY)
  private Set<Unit> units = new HashSet<>();

  @OneToMany(mappedBy = "rentalProfile", fetch = FetchType.LAZY)
  private Set<Membership> memberships = new HashSet<>();
}
