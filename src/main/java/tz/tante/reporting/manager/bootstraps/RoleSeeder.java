package tz.tante.reporting.manager.bootstraps;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tz.tante.reporting.manager.enums.AuthorityRoleName;
import tz.tante.reporting.manager.enums.BusinessMembershipRoleName;
import tz.tante.reporting.manager.models.entities.AuthorityRole;
import tz.tante.reporting.manager.models.entities.BusinessMembershipRole;
import tz.tante.reporting.manager.repositories.AuthorityRoleRepository;
import tz.tante.reporting.manager.repositories.BusinessMembershipRoleRepository;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner
{
  private final BusinessMembershipRoleRepository businessMembershipRoleRepository;
  private final AuthorityRoleRepository authorityRoleRepository;

  @Override
  public void run(String... args)
  {
    for (BusinessMembershipRoleName businessMembershipRoleName : BusinessMembershipRoleName.values())
    {
      if (!businessMembershipRoleRepository.existsByName(businessMembershipRoleName))
      {
        BusinessMembershipRole businessMembershipRole = new BusinessMembershipRole();
        businessMembershipRole.setName(businessMembershipRoleName);
        businessMembershipRoleRepository.save(businessMembershipRole);
      }
    }

    for (AuthorityRoleName authorityRoleName : AuthorityRoleName.values())
    {
      if (!authorityRoleRepository.existsByName(authorityRoleName))
      {
        AuthorityRole authorityRole = new AuthorityRole();
        authorityRole.setName(authorityRoleName);
        authorityRoleRepository.save(authorityRole);
      }
    }
  }



}