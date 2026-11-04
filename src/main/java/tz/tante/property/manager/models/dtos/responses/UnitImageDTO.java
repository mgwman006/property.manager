package tz.tante.property.manager.models.dtos.responses;

public record UnitImageDTO(
  Long id,
  String url,
  String fileName,
  String contentType,
  Long size,
  boolean primaryImage
)
{
}
