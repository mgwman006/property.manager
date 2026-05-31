package tz.tante.reporting.manager.models.dtos.requests.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload to Authenticate account")
public record AccountAuthRequestDTO(
  @Schema(example = "+255612627337")
  @NotBlank(message = "Phone number is required")
  @Pattern(
    regexp = "^(\\+255|255|0)([678])\\d{8}$",
    message = "Invalid Tanzanian phone number"
  )
  String phoneNumber,

  @Schema(example = "1234")
  @NotBlank(message = "Password is required")
  @Size(min = 4, message = "Password must be at least 4 characters")
  String passWord
){ }
