package tz.tante.property.manager.models.dtos.requests;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import tz.tante.property.manager.enums.DevelopmentStatus;
import tz.tante.property.manager.enums.PropertyCategory;
import tz.tante.property.manager.enums.PropertyStatus;
import tz.tante.property.manager.models.dtos.AddressDTO;

@Schema(description = "Payload to create a new property")
public record PropertyCreateDTO(
  String description,
  @NotBlank(message = "Creator Id is required")
  Long creatorId,
  @NotBlank(message = "Property name is required")
  String name,
  Long landSize,
  String landSizeUnit,
  @NotBlank(message = "Latitude is required")
  Double latitude,
  @NotBlank(message = "Longitude is required")
  Double longitude,
  AddressDTO address,
  @NotBlank(message = "Property category is required")
  PropertyCategory type,
  DevelopmentStatus developmentStatus,
  PropertyStatus status
)
{
}
