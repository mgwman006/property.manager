package tz.tante.property.manager.models.dtos.requests;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tz.tante.property.manager.enums.DevelopmentStatus;
import tz.tante.property.manager.enums.PropertyCategory;
import tz.tante.property.manager.models.entities.PropertyOwner;

import java.util.List;

@Schema(description = "Payload to create a new property")
public record PropertyCreateDTO(

  String description,

  @NotNull(message = "Creator Id is required")
  Long createdByUserId,

  @NotBlank(message = "Property name is required")
  String name,

  Double landSize,

  String landSizeUnit,

  @NotNull(message = "Location is required")
  LocationCreateDTO location,

  @NotNull(message = "Property category is required")
  PropertyCategory type,

  DevelopmentStatus developmentStatus,

  @NotNull(message = "Property owners are required")
  List<PropertyOwnerDTO> owners

)
{
}
