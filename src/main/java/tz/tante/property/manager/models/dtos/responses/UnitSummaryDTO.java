package tz.tante.property.manager.models.dtos.responses;


import io.swagger.v3.oas.annotations.media.Schema;
import tz.tante.property.manager.enums.UnitStatus;
import tz.tante.property.manager.enums.UnitType;

import java.math.BigDecimal;
import java.util.List;

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
  Long roomSize,
  String sizeUnit,
  boolean isListed,
  int floorNumber,
  Long buildingId,
  String propertyName,
  LocationDetailsDTO location,
  List<UnitImageDTO> images)
{
}
