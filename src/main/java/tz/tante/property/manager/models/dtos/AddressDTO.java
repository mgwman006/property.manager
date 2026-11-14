package tz.tante.property.manager.models.dtos;

public record AddressDTO(
  Long postalCode,
  Long houseNumber,
  String streetName,
  String ward,
  String city,
  String region,
  String country)
{
}
