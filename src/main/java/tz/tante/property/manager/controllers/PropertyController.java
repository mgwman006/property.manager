package tz.tante.property.manager.controllers;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tz.tante.property.manager.models.dtos.ApiResponse;
import tz.tante.property.manager.models.dtos.requests.PropertyCreateDTO;
import tz.tante.property.manager.models.dtos.responses.PropertyDetailsDTO;
import tz.tante.property.manager.services.PropertyService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/properties")
@SecurityRequirement(name = "bearerAuth")
public class PropertyController
{
  private final PropertyService propertyService;

  @PostMapping
  public ResponseEntity<ApiResponse<PropertyDetailsDTO>> registerProperty(@Valid @RequestBody PropertyCreateDTO request)
  {
    PropertyDetailsDTO response = propertyService.registerProperty(request);

    return ResponseEntity.status(HttpStatus.CREATED)
      .body(ApiResponse.success(response,HttpStatus.CREATED.value()));
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<PropertyDetailsDTO>>> getAllProperties()
  {
    List<PropertyDetailsDTO> response = propertyService.getAllProperties();
    return ResponseEntity.status(HttpStatus.OK)
      .body(ApiResponse.success(response, HttpStatus.OK.value()));
  }

  @GetMapping("{propertyId}")
  public ResponseEntity<ApiResponse<PropertyDetailsDTO>> getProperty(@PathVariable Long propertyId)
  {
    PropertyDetailsDTO response = propertyService.getPropertyDetailsById(propertyId);
    return ResponseEntity.status(HttpStatus.OK)
      .body(ApiResponse.success(response, HttpStatus.OK.value()));
  }
}
