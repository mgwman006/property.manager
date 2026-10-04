package tz.tante.property.manager.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tz.tante.property.manager.models.dtos.ApiResponse;
import tz.tante.property.manager.models.dtos.requests.BuildingDetailsUpdateDTO;
import tz.tante.property.manager.models.dtos.responses.BuildingDetailsDTO;
import tz.tante.property.manager.services.BuildingService;

@RestController
@RequestMapping("/v1/buildings")
@AllArgsConstructor
public class BuildingController
{
  private final BuildingService buildingService;

  @PatchMapping("/{buildingId}")
  public ResponseEntity<ApiResponse<BuildingDetailsDTO>> updateBuildingDetails(Long buildingId, BuildingDetailsUpdateDTO request)
  {
    BuildingDetailsDTO updatedBuilding = buildingService.updateBuildingDetails(buildingId, request);
    return ResponseEntity
      .status(200)
      .body(ApiResponse.success(updatedBuilding, 200));
  }
}
