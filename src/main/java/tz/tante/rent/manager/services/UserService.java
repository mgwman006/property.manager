package tz.tante.rent.manager.services;

import lombok.AllArgsConstructor;
import tz.tante.rent.manager.enums.AuthorityRoleName;
import tz.tante.rent.manager.exceptions.TanteException;
import tz.tante.rent.manager.exceptions.ResourceNotFoundException;
import tz.tante.rent.manager.models.dtos.requests.Users.UserCreateRequestDto;
import tz.tante.rent.manager.models.dtos.responses.MembershipDetailsDTO;
import tz.tante.rent.manager.models.dtos.responses.users.UserDetailsDTO;
import tz.tante.rent.manager.models.entities.Account;
import tz.tante.rent.manager.models.entities.AuthorityRole;
import tz.tante.rent.manager.models.entities.Tenant;
import tz.tante.rent.manager.models.entities.User;
import tz.tante.rent.manager.repositories.AccountRepository;
import tz.tante.rent.manager.repositories.AuthorityRoleRepository;
import tz.tante.rent.manager.repositories.UserRepository;
import org.springframework.stereotype.Service;
import tz.tante.rent.manager.utilities.Utils;
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
      Account account = accountRepository.findByPhoneNumber(normalizedPhoneNumber)
        .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

      User user = account.getUser();
      if (user == null)
      {
        throw new ResourceNotFoundException("User not found");
      }

      Tenant tenant = user.getTenantProfile();

      return new UserDetailsDTO(
        user.getId(),
        user.getFirstName(),
        user.getLastName(),
        account.getPhoneNumber(),
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
      Account account = accountRepository.findByPhoneNumber(normalizedPhoneNumber)
        .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

      AuthorityRole role = authorityRoleRepository.findByName(AuthorityRoleName.ROLE_USER);
      account.addAuthorityRole(role);

      User user = new User(request.firstName(), request.lastName());
      user.setAccount(account);

      account.setUser(user);

      user = userRepository.save(user);

      return new UserDetailsDTO(
        user.getId(),
        user.getFirstName(),
        user.getLastName(),
        user.getAccount().getPhoneNumber(),
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
