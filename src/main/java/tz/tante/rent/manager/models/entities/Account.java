package tz.tante.rent.manager.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "accounts")
public class Account extends BaseEntity
{
  @Column(unique = true)
  private String email;

  @Column(nullable = false, unique = true)
  private String phoneNumber;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private boolean enabled = true;

  @Column(nullable = false)
  private boolean emailVerified = false;

  @Column(nullable = false)
  private boolean phoneNumberVerified = false;

  @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
  private User user;


  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "account_authority",
    joinColumns = @JoinColumn(name = "account_id"),
    inverseJoinColumns = @JoinColumn(name = "authority_id")
  )
  private Set<AuthorityRole> authorityRoles = new HashSet<>();

  public Account(String phoneNumber, String password)
  {
    this.phoneNumber = phoneNumber;
    this.password = password;
  }

  public void addAuthorityRole(AuthorityRole authorityRole)
  {
    this.authorityRoles.add(authorityRole);
  }
}
