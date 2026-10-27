package tz.tante.property.manager.models.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tz.tante.property.manager.enums.UnitStatus;
import tz.tante.property.manager.enums.UnitType;

import java.math.BigDecimal;

@Schema(description = "Data Transfer Object for creating a new unit within a property management system. This DTO captures all necessary information required to create a unit, including its characteristics and association with a rental profile.")
public record UnitCreateDTO(
  String unitNumber,
  @NotNull(message = "Rental Profile Id is required")
  Long rentalProfileId,
  int numberOfBedrooms,
  int numberOfBathrooms,
  int numberParkingSpots,
  @NotNull(message = "Rent amount is required")
  BigDecimal rentAmount,
  UnitType type,
  UnitStatus status,
  Long roomSize,
  String sizeUnit)
{
}
