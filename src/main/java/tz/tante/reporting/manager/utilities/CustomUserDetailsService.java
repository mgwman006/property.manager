package tz.tante.reporting.manager.utilities;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tz.tante.reporting.manager.models.entities.Project;
import tz.tante.reporting.manager.repositories.AccountRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService
{
  private final AccountRepository accountRepository;

  @Override
  public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException
  {
    Project project = accountRepository.findByPhoneNumber(phoneNumber)
      .orElseThrow(() -> new UsernameNotFoundException("Project with phoneNumber: " + phoneNumber+" does not exist"));

    return org.springframework.security.core.userdetails.User
      .withUsername(project.getPhoneNumber())
      .password(project.getPassword())
      .disabled(!project.isEnabled())
      .authorities(
        project.getAuthorityRoles()
          .stream()
          .map(authorityRole -> new SimpleGrantedAuthority(authorityRole.getName().name()))
          .toList()
      ).build();
  }
}
