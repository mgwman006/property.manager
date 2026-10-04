package tz.tante.property.manager.models.dtos.requests;

public record BuildingDetailsUpdateDTO(
  String name,
  int number,
  String code,
  String description,
  int numberOfFloors)
{
}


