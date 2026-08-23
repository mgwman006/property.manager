package tz.tante.property.manager.models.dtos.requests;

import tz.tante.property.manager.enums.PropertyOwnerType;

import java.math.BigDecimal;

public record PropertyOwnerDTO(
  Long ownerId,
  PropertyOwnerType ownerType,
  BigDecimal ownershipPercentage
  )
{
}
