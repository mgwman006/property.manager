package tz.tante.property.manager.models.dtos.responses;


import java.math.BigDecimal;

public record UnitDetailsDTO(
  Long id,
  String unitNumber,
  BigDecimal rentAmount,
  String type,
  String status,
  Long size,
  String sizeUnit,
  Long propertyId)
{
}
