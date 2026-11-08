package tz.tante.property.manager.models.dtos.responses;

public record BuildingSummaryDTO(
  Long id,
  String name,
  int number,
  String code,
  String description,
  int numberOfFloors,
  PropertySummaryDTO property)
{
}
