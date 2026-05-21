package tz.tante.rent.manager.services;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import tz.tante.rent.manager.enums.BusinessMembershipRoleName;
import tz.tante.rent.manager.exceptions.TanteException;
import tz.tante.rent.manager.exceptions.ResourceNotFoundException;
import tz.tante.rent.manager.models.entities.BusinessMembershipRole;
import tz.tante.rent.manager.repositories.BusinessMembershipRoleRepository;

@Getter
@Setter
@AllArgsConstructor
@Service
public class RoleService
{
  private final BusinessMembershipRoleRepository businessMembershipRoleRepository;

  public BusinessMembershipRole getRoleByName(BusinessMembershipRoleName businessMembershipRoleName)
  {
    try
    {
      return businessMembershipRoleRepository.findByName(businessMembershipRoleName)
        .orElseThrow(() -> new ResourceNotFoundException("BusinessMembershipRole not found: " + businessMembershipRoleName));
    }
    catch (Exception exception)
    {
      throw new TanteException(exception.getMessage());
    }
  }
}
