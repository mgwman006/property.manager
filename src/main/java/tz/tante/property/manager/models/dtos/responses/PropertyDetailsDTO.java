package tz.tante.property.manager.models.dtos.responses;

import tz.tante.property.manager.models.dtos.AddressDTO;

public record PropertyDetailsDTO(
  Long id,
  String description,
  Long managingOrganizationId,
  Long creatorId,
  String name,
  Long landSize,
  String landSizeUnit,
  Double latitude,
  Double longitude,
  AddressDTO address,
  String type,
  String developmentStatus,
  String status)
{
}
