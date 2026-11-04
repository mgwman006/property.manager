package tz.tante.property.manager.controllers;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tz.tante.property.manager.models.dtos.ApiResponse;
import tz.tante.property.manager.models.dtos.requests.PropertyCreateDTO;
import tz.tante.property.manager.models.dtos.responses.PropertyDetailsDTO;
import tz.tante.property.manager.models.dtos.responses.PropertyImageDTO;
import tz.tante.property.manager.services.PropertyService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/properties")
@SecurityRequirement(name = "bearerAuth")
public class PropertyController
{
  private final PropertyService propertyService;

  @PostMapping("/rental-profile/{rentalProfileId}")
  public ResponseEntity<ApiResponse<PropertyDetailsDTO>> registerProperty(@PathVariable Long rentalProfileId, @Valid @RequestBody PropertyCreateDTO request)
  {
    PropertyDetailsDTO response = propertyService.registerPropertyByRentalProfileId(rentalProfileId, request);
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

  @GetMapping("/{propertyId}")
  public ResponseEntity<ApiResponse<PropertyDetailsDTO>> getProperty(@PathVariable Long propertyId)
  {
    PropertyDetailsDTO response = propertyService.getPropertyDetailsById(propertyId);
    return ResponseEntity.status(HttpStatus.OK)
      .body(ApiResponse.success(response, HttpStatus.OK.value()));
  }

  @GetMapping("/rental-profile/{rentalProfileId}")
  public ResponseEntity<ApiResponse<List<PropertyDetailsDTO>>> getPropertiesByRentalProfileId(@PathVariable Long rentalProfileId)
  {
    List<PropertyDetailsDTO> response = propertyService.getPropertiesByRentalProfileId(rentalProfileId);
    return ResponseEntity.status(HttpStatus.OK)
      .body(ApiResponse.success(response, HttpStatus.OK.value()));
  }

  @PostMapping("/{propertyId}/images")
  public ResponseEntity<ApiResponse<PropertyImageDTO>> uploadPropertyImage(
    @PathVariable Long propertyId,
    @RequestParam("file") MultipartFile file,
    @RequestParam(required = false) Long createdByUserId
  )
  {
    PropertyImageDTO response = propertyService.uploadPropertyImage(propertyId, file, createdByUserId);
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(ApiResponse.success(response, HttpStatus.CREATED.value()));
  }

  @GetMapping("/{propertyId}/images")
  public ResponseEntity<ApiResponse<List<PropertyImageDTO>>> getPropertyImages(@PathVariable Long propertyId)
  {
    List<PropertyImageDTO> response = propertyService.getPropertyImages(propertyId);
    return ResponseEntity.status(HttpStatus.OK)
      .body(ApiResponse.success(response, HttpStatus.OK.value()));
  }

  @DeleteMapping("/{propertyId}/images/{imageId}")
  public ResponseEntity<ApiResponse<Void>> deletePropertyImage(@PathVariable Long propertyId, @PathVariable Long imageId)
  {
    propertyService.deletePropertyImage(propertyId, imageId);
    return ResponseEntity.status(HttpStatus.OK)
      .body(ApiResponse.success(null, HttpStatus.OK.value()));
  }
}
