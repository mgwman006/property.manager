package tz.tante.reporting.manager.models.dtos.responses;

public record AccountAuthResponseDTO(
  String  phoneNumber,
  String jwtToken
) {}
