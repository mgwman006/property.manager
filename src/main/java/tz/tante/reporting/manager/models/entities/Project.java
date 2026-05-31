package tz.tante.reporting.manager.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.tante.reporting.manager.enums.ProjectStatus;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "projects")
public class Project extends BaseEntity
{
  @Column(nullable = false)
  private Long organizationId;

  @Column(nullable = false, unique = true)
  private String code;

  private String name;

  @Enumerated(EnumType.STRING)
  private ProjectStatus status;

  private LocalDateTime startDate;
  private LocalDateTime expectedEndDate;
  private LocalDateTime actualEndDate;

  @Column(nullable = false)
  private boolean emailVerified = false;

  @Column(nullable = false)
  private boolean phoneNumberVerified = false;

  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
  private Set<Site> sites = new HashSet<>();


  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "account_authority",
    joinColumns = @JoinColumn(name = "account_id"),
    inverseJoinColumns = @JoinColumn(name = "authority_id")
  )
  private Set<AuthorityRole> authorityRoles = new HashSet<>();

  public Project(String phoneNumber, String password)
  {
    this.phoneNumber = phoneNumber;
    this.password = password;
  }

  public void addAuthorityRole(AuthorityRole authorityRole)
  {
    this.authorityRoles.add(authorityRole);
  }
}
