package tz.tante.reporting.manager.models.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tz.tante.reporting.manager.enums.BusinessMembershipRoleName;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "business_membership_roles")
public class BusinessMembershipRole extends BaseEntity
{
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, unique = true)
  private BusinessMembershipRoleName name;

  @ManyToMany(mappedBy = "businessMembershipRoles", fetch = FetchType.LAZY)
  private Set<Membership> memberships = new HashSet<>();
}
