package tz.tante.property.manager.models.dtos.responses;

import tz.tante.property.manager.models.dtos.AddressDTO;

public record LocationDetailsDTO(
  Double latitude,
  Double longitude,
  AddressDTO address
)
{
}
