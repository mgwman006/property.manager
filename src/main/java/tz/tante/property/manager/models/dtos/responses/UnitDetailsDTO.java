package tz.tante.property.manager.models.dtos.responses;


import java.math.BigDecimal;

public record UnitDetailsDTO(
  Long id,
  Long rentalProfileId,
  int numberOfBedrooms,
  int numberOfBathrooms,
  int numberParkingSpots,
  String unitNumber,
  BigDecimal rentAmount,
  String type,
  String status,
  Long size,
  String sizeUnit,
  Long propertyId)
{
}
