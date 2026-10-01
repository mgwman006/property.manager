package tz.tante.property.manager.models.dtos;

public record AddressDTO(
  long postalCode,
  long streetNumber,
  String streetName,
  String city,
  String ward,
  String region,
  String country,
  String popularAreaName
)
{
}
