package tz.tante.property.manager.models.dtos.responses;


public record PropertyDetailsDTO(
  Long id,
  String code,
  String description,
  String name,
  Double landSize,
  String landSizeUnit,
  String type,
  String developmentStatus
)
{
}
