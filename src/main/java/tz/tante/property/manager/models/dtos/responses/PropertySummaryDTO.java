package tz.tante.property.manager.models.dtos.responses;

import tz.tante.property.manager.enums.DevelopmentStatus;
import tz.tante.property.manager.enums.PropertyType;

import java.util.List;

public record PropertySummaryDTO(
  Long id,
  String name,
  String code,
  String description,
  Double landSize,
  String landSizeUnit,
  LocationDetailsDTO location,
  PropertyType type,
  DevelopmentStatus developmentStatus,
  List<PropertyImageDTO> images
)
{
}
