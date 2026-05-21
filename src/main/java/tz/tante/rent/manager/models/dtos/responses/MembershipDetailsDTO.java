package tz.tante.rent.manager.models.dtos.responses;

import java.util.List;

public record MembershipDetailsDTO(
  Long id,
  Long userId,
  Long rentalProfileId,
  List<String> businessRoles
)
{ }
