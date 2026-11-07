package tz.tante.property.manager.models.dtos.requests;

import jakarta.validation.constraints.NotNull;

public record UnitListingUpdateDTO(
  @NotNull(message = "listed is required") Boolean listed
)
{
}
