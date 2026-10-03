package tz.tante.property.manager.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tz.tante.property.manager.enums.UnitStatus;
import tz.tante.property.manager.models.dtos.ApiResponse;
import tz.tante.property.manager.models.dtos.requests.UnitCreateDTO;
import tz.tante.property.manager.models.dtos.responses.UnitDetailsDTO;
import tz.tante.property.manager.services.UnitService;

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

}
