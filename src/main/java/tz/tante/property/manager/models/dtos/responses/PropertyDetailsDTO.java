package tz.tante.property.manager.models.dtos.responses;


import tz.tante.property.manager.enums.DevelopmentStatus;
import tz.tante.property.manager.enums.PropertyType;

import java.util.List;

public record PropertyDetailsDTO(
  Long id,
  String code,
  String description,
  String name,
  Double landSize,
  String landSizeUnit,
  PropertyType type,
  DevelopmentStatus developmentStatus,
  LocationDetailsDTO location,
  List<BuildingDetailsDTO> buildings,
  List<PropertyImageDTO> images
)
{
}
