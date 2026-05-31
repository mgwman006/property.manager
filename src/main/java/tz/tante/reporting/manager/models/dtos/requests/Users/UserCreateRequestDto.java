package tz.tante.reporting.manager.models.dtos.requests.Users;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Payload to create new user")
public record UserCreateRequestDto(
  @Schema(example = "Maneno")
  @NotBlank(message = "First Name is required")
  String firstName,

  @Schema(example = "Mgwami")
  @NotBlank(message = "Last Name is required")
  String lastName,

  @Pattern(
    regexp = "^(\\+255|255|0)([678])\\d{8}$",
    message = "Invalid Tanzanian phone number"
  )
  String phoneNumber) {}
