package tz.tante.reporting.manager.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity
{
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @OneToOne
    @JoinColumn(name = "account_id", unique = true)
    private Project project;

    @OneToOne(mappedBy = "user")
    private Tenant tenantProfile;

    @OneToMany(mappedBy = "user")
    Set<Membership> memberships = new HashSet<>();

    public User(String firstName, String lastName)
    {
        this.firstName = firstName;
        this.lastName = lastName;
    }

}