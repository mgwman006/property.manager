package tz.tante.property.manager.models.dtos.responses;

import java.util.List;

public record ListedUnitsPageDTO(
  List<UnitDetailsDTO> items,
  int page,
  int size,
  long totalElements,
  int totalPages,
  boolean hasNext,
  boolean hasPrevious
)
{
}
