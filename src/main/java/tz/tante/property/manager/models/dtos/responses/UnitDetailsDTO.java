package tz.tante.property.manager.models.dtos.responses;


import tz.tante.property.manager.enums.UnitStatus;
import tz.tante.property.manager.enums.UnitType;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

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
  Long roomSize,
  String sizeUnit,
  boolean isListed,
  int floorNumber,
  BuildingSummaryDTO building,
  List<UnitImageDTO> images,
  List<Long> agents)
{
}
