package tz.tante.property.manager.controllers;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tz.tante.property.manager.models.dtos.ApiResponse;
import tz.tante.property.manager.models.dtos.requests.PropertyCreateDTO;
import tz.tante.property.manager.models.dtos.responses.PropertyDetailsDTO;
import tz.tante.property.manager.services.PropertyService;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/properties")
public class PropertyController
{
  private final PropertyService propertyService;

  @PostMapping
  public ResponseEntity<ApiResponse<PropertyDetailsDTO>> registerProperty(@RequestBody PropertyCreateDTO request)
  {
    PropertyDetailsDTO response = propertyService.registerProperty(request);

    return ResponseEntity.status(HttpStatus.CREATED)
      .body(ApiResponse.success(response,HttpStatus.CREATED.value()));
  }
}
