package tz.tante.property.manager.models.dtos;

public record AddressDTO(
  String street,
  String area,
  String city,
  String region,
  String country
)
{
}
