package tz.tante.property.manager.models.dtos;

public record AddressDTO(
  long postalCode,
  long streetNumber,
  String streetName,
  String ward,
  String city,
  String region,
  String country)
{
}
