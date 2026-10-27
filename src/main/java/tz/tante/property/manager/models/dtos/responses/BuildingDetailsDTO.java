package tz.tante.property.manager.models.dtos.responses;

import java.util.List;

public record BuildingDetailsDTO(
  Long propertyId,
  Long id,
  String name,
  int number,
  String code,
  String description,
  int numberOfFloors,
  List<UnitDetailsDTO> units)
{
}
