package tz.tante.reporting.manager.models.dtos.responses;

public record LandLordResponseDto(
  Long id,
  String firstName,
  String lastName,
  String phoneNumber,
  String email
) {}
