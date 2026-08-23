package tz.tante.property.manager.models.dtos.requests;

import jakarta.validation.constraints.NotNull;
import tz.tante.property.manager.models.dtos.AddressDTO;

public record LocationCreateDTO(
  String name,
  @NotNull(message = "Latitude is required")
  double latitude,
  @NotNull(message = "Longitude is required")
  double longitude,
  AddressDTO address)
{
}
