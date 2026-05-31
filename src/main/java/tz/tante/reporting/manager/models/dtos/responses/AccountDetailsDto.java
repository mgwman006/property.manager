package tz.tante.reporting.manager.models.dtos.responses;


public record AccountDetailsDto(
  Long id,
  String phoneNumber,
  String email,
  boolean enabled
) { }
