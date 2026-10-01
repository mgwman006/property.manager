package tz.tante.property.manager.models.dtos.responses;


import java.util.List;

public record PropertyDetailsDTO(
  Long id,
  String code,
  String description,
  String name,
  Double landSize,
  String landSizeUnit,
  String type,
  String developmentStatus,
  LocationDetailsDTO location,
  List<BuildingDetailsDTO> buildings
)
{
}
