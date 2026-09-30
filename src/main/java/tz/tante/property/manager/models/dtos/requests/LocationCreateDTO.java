package tz.tante.property.manager.models.dtos.requests;

import jakarta.validation.constraints.NotNull;
import tz.tante.property.manager.models.dtos.AddressDTO;

public record LocationCreateDTO(
  String name,
  double latitude,
  double longitude,
  @NotNull(message = "Address is required")
  AddressDTO address)
{
}
