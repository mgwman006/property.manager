package tz.tante.reporting.manager.services;

import lombok.AllArgsConstructor;
import tz.tante.reporting.manager.enums.AuthorityRoleName;
import tz.tante.reporting.manager.exceptions.TanteException;
import tz.tante.reporting.manager.exceptions.ResourceNotFoundException;
import tz.tante.reporting.manager.models.dtos.requests.Users.UserCreateRequestDto;
import tz.tante.reporting.manager.models.dtos.responses.MembershipDetailsDTO;
import tz.tante.reporting.manager.models.dtos.responses.users.UserDetailsDTO;
import tz.tante.reporting.manager.models.entities.Project;
import tz.tante.reporting.manager.models.entities.AuthorityRole;
import tz.tante.reporting.manager.models.entities.Tenant;
import tz.tante.reporting.manager.models.entities.User;
import tz.tante.reporting.manager.repositories.AccountRepository;
import tz.tante.reporting.manager.repositories.AuthorityRoleRepository;
import tz.tante.reporting.manager.repositories.UserRepository;
import org.springframework.stereotype.Service;
import tz.tante.reporting.manager.utilities.Utils;
import java.util.ArrayList;


@AllArgsConstructor
@Service
public class UserService {

  private final AccountRepository accountRepository;
  private final UserRepository userRepository;
  private final AuthorityRoleRepository authorityRoleRepository;

  public UserDetailsDTO getUserByPhoneNumber(String phoneNumber)
  {
    try
    {
      String normalizedPhoneNumber = Utils.normalizePhone(phoneNumber);
      Project project = accountRepository.findByPhoneNumber(normalizedPhoneNumber)
        .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

      User user = project.getUser();
      if (user == null)
      {
        throw new ResourceNotFoundException("User not found");
      }

      Tenant tenant = user.getTenantProfile();

      return new UserDetailsDTO(
        user.getId(),
        user.getFirstName(),
        user.getLastName(),
        project.getPhoneNumber(),
        tenant == null ? null: tenant.getId(),
        user.getMemberships()
          .stream()
          .map(membership -> new MembershipDetailsDTO(
            membership.getId(),
            membership.getUser() == null ? null:membership.getUser().getId(),
            membership.getRentalProfile() == null ? null:membership.getRentalProfile().getId(),
            membership.getBusinessMembershipRoles()
              .stream()
              .map(r -> r.getName().name())
              .toList()
          ))
          .toList()
      );
    }
    catch (ResourceNotFoundException exception)
    {
      throw exception;
    }
    catch (Exception exception)
    {
      throw new TanteException(exception.getMessage());
    }
  }

  public UserDetailsDTO createUser(UserCreateRequestDto request)
  {
    try
    {
      String normalizedPhoneNumber = Utils.normalizePhone(request.phoneNumber());
      Project project = accountRepository.findByPhoneNumber(normalizedPhoneNumber)
        .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

      AuthorityRole role = authorityRoleRepository.findByName(AuthorityRoleName.ROLE_USER);
      project.addAuthorityRole(role);

      User user = new User(request.firstName(), request.lastName());
      user.setProject(project);

      project.setUser(user);

      user = userRepository.save(user);

      return new UserDetailsDTO(
        user.getId(),
        user.getFirstName(),
        user.getLastName(),
        user.getProject().getPhoneNumber(),
        null,
        new ArrayList<>()
      );
    }
    catch (ResourceNotFoundException exception)
    {
      throw exception;
    }
    catch (Exception exception)
    {
      throw new TanteException(exception.getMessage());
    }
  }
}
