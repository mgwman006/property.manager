package tz.tante.reporting.manager.models.dtos.requests.landlord;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "RentalProfile request payload")
public record LandLordRequestDto(

  @Schema(example = "John")
  @NotBlank(message = "First name is required")
  String firstName,

  @Schema(example = "Doe")
  @NotBlank(message = "Last name is required")
  String lastName,

  @Schema(example = "+27821234567")
  @NotBlank(message = "Phone number is required")
  @Pattern(
    regexp = "^\\+?[0-9]{10,15}$",
    message = "Invalid phone number"
  )
  String phoneNumber,

  @Schema(example = "john@example.com")
  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  String email,

  @Schema(example = "123456")
  @NotBlank(message = "Password is required")
  @Size(min = 6, message = "Password must be at least 6 characters")
  String passWord
) { }
