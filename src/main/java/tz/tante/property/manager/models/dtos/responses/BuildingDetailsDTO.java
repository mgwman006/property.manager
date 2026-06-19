package tz.tante.property.manager.models.dtos.responses;

import java.util.List;

public record BuildingDetailsDTO(
  Long id,
  String code,
  String name,
  String description,
  Long propertyId,
  List<UnitDetailsDTO> units)
{
}
