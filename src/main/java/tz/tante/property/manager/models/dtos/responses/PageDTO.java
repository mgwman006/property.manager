package tz.tante.property.manager.models.dtos.responses;

import java.util.List;

public record PageDTO<T>(
  List<T> items,
  int page,
  int size,
  long totalElements,
  int totalPages,
  boolean hasNext,
  boolean hasPrevious
)
{
}
