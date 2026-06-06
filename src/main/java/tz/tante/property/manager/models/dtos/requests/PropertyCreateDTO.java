package tz.tante.property.manager.models.dtos.requests;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import tz.tante.property.manager.enums.DevelopmentStatus;
import tz.tante.property.manager.enums.PropertyCategory;
import tz.tante.property.manager.enums.PropertyStatus;
import tz.tante.property.manager.models.dtos.AddressDTO;
import tz.tante.property.manager.models.entities.Address;

public record PropertyCreateDTO(
  String description,
  Long managingOrganizationId,
  Long creatorId,
  String name,
  Long landSize,
  String landSizeUnit,
  Double latitude,
  Double longitude,
  AddressDTO address,
  PropertyCategory type,
  DevelopmentStatus developmentStatus,
  PropertyStatus status
)
{
}
