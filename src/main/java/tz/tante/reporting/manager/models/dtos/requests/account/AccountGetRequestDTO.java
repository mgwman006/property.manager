package tz.tante.reporting.manager.models.dtos.requests.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AccountGetRequestDTO(
  @Schema(example = "+255612627337")
  @NotBlank(message = "Phone number is required")
  @Pattern(
    regexp = "^(\\+255|255|0)([678])\\d{8}$",
    message = "Invalid Tanzanian phone number"
  )
  String phoneNumber
) { }
