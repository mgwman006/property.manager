package tz.tante.property.manager.models.dtos.responses;


import io.swagger.v3.oas.annotations.media.Schema;
import tz.tante.property.manager.enums.UnitStatus;
import tz.tante.property.manager.enums.UnitType;

import java.math.BigDecimal;

@Schema(description = "Data Transfer Object representing the details of a unit within a property management system.")
public record UnitDetailsDTO(
  Long id,
  String unitNumber,
  Long rentalProfileId,
  int numberOfBedrooms,
  int numberOfBathrooms,
  int numberParkingSpots,
  BigDecimal rentAmount,
  UnitType type,
  UnitStatus status,
  Long size,
  String sizeUnit,
  Long buildingId)
{
}
