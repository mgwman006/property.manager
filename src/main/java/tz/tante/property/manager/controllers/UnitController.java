package tz.tante.property.manager.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tz.tante.property.manager.enums.UnitStatus;
import tz.tante.property.manager.models.dtos.ApiResponse;
import tz.tante.property.manager.models.dtos.requests.UnitCreateDTO;
import tz.tante.property.manager.models.dtos.responses.UnitDetailsDTO;
import tz.tante.property.manager.models.dtos.responses.UnitImageDTO;
import tz.tante.property.manager.services.UnitService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/units")
public class UnitController
{
  private final UnitService unitService;

  @PostMapping("/building/{buildingId}")
  public ResponseEntity<ApiResponse<UnitDetailsDTO>> createUnit(@PathVariable Long buildingId, @Valid @RequestBody UnitCreateDTO request)
  {
    UnitDetailsDTO response = unitService.createUnit(buildingId, request);
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(ApiResponse.success(response, HttpStatus.CREATED.value()));
  }

  @GetMapping("/{unitId}/status")
  public ResponseEntity<ApiResponse<UnitStatus>> getUnitStatus(@PathVariable Long unitId)
  {
    UnitStatus status = unitService.getUnitStatus(unitId);
    return ResponseEntity.ok(ApiResponse.success(status, HttpStatus.OK.value()));
  }

  @PatchMapping("/{unitId}/status")
  public ResponseEntity<ApiResponse<UnitStatus>> updateUnitStatus(@PathVariable Long unitId, @RequestParam UnitStatus status)
  {
    UnitStatus updatedStatus = unitService.updateUnitStatus(unitId, status);
    return ResponseEntity.ok(ApiResponse.success(updatedStatus, HttpStatus.OK.value()));
  }

  @PostMapping("/{unitId}/images")
  public ResponseEntity<ApiResponse<UnitImageDTO>> uploadUnitImage(
    @PathVariable Long unitId,
    @RequestParam("file") MultipartFile file,
    @RequestParam(required = false) Long createdByUserId
  )
  {
    UnitImageDTO response = unitService.uploadUnitImage(unitId, file, createdByUserId);
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(ApiResponse.success(response, HttpStatus.CREATED.value()));
  }

  @GetMapping("/{unitId}/images")
  public ResponseEntity<ApiResponse<List<UnitImageDTO>>> getUnitImages(@PathVariable Long unitId)
  {
    List<UnitImageDTO> response = unitService.getUnitImages(unitId);
    return ResponseEntity.status(HttpStatus.OK)
      .body(ApiResponse.success(response, HttpStatus.OK.value()));
  }

  @DeleteMapping("/{unitId}/images/{imageId}")
  public ResponseEntity<ApiResponse<Void>> deleteUnitImage(@PathVariable Long unitId, @PathVariable Long imageId)
  {
    unitService.deleteUnitImage(unitId, imageId);
    return ResponseEntity.status(HttpStatus.OK)
      .body(ApiResponse.success(null, HttpStatus.OK.value()));
  }

}
