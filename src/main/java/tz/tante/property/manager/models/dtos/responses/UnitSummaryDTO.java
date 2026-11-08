package tz.tante.property.manager.models.dtos.responses;


import io.swagger.v3.oas.annotations.media.Schema;
import tz.tante.property.manager.enums.UnitStatus;
import tz.tante.property.manager.enums.UnitType;

import java.math.BigDecimal;

@Schema(description = "Lightweight representation of a unit, used when returning lists of units.")
public record UnitSummaryDTO(
  Long id,
  String unitNumber,
  int numberOfBedrooms,
  int numberOfBathrooms,
  int numberParkingSpots,
  BigDecimal rentAmount,
  UnitType type,
  UnitStatus status,
  Long size,
  String sizeUnit,
  boolean isListed,
  Long buildingId,
  @Schema(description = "Primary image of the unit, or the first available image when no primary image is set")
  UnitImageDTO coverImage)
{
}
