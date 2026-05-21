package tz.tante.rent.manager.models.dtos.responses.users;

import tz.tante.rent.manager.models.dtos.responses.MembershipDetailsDTO;
import java.util.List;

public record UserDetailsDTO(
  Long id,
  String firstName,
  String lastName,
  String phoneNumber,
  Long tenantId,
  List<MembershipDetailsDTO> memberships
)
{
}
