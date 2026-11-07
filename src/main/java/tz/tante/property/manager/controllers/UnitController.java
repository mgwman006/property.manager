package tz.tante.property.manager.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tz.tante.property.manager.enums.UnitStatus;
import tz.tante.property.manager.enums.UnitType;
import tz.tante.property.manager.models.dtos.ApiResponse;
import tz.tante.property.manager.models.dtos.requests.UnitCreateDTO;
import tz.tante.property.manager.models.dtos.requests.UnitListingUpdateDTO;
import tz.tante.property.manager.models.dtos.responses.ListedUnitsPageDTO;
import tz.tante.property.manager.models.dtos.responses.UnitDetailsDTO;
import tz.tante.property.manager.models.dtos.responses.UnitImageDTO;
import tz.tante.property.manager.services.UnitService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/units")
public class UnitController
{
  private final UnitService unitService;

  @GetMapping
  public ResponseEntity<ApiResponse<ListedUnitsPageDTO>> getListedUnits(
    @RequestParam(required = false, defaultValue = "0") Integer page,
    @RequestParam(required = false, defaultValue = "20") Integer size,
    @RequestParam(required = false, defaultValue = "rentAmount") String sortBy,
    @RequestParam(required = false, defaultValue = "desc") String direction,
    @RequestParam(required = false) Boolean listed,
    @RequestParam(required = false) Long buildingId,
    @RequestParam(required = false) Long rentalProfileId,
    @RequestParam(required = false) UnitType type,
    @RequestParam(required = false) UnitStatus status,
    @RequestParam(required = false) String city,
    @RequestParam(required = false) String region,
    @RequestParam(required = false) String ward,
    @RequestParam(required = false) String streetName,
    @RequestParam(required = false) String country,
    @RequestParam(required = false) Integer minBedrooms,
    @RequestParam(required = false) Integer maxBedrooms,
    @RequestParam(required = false) Integer minBathrooms,
    @RequestParam(required = false) Integer maxBathrooms,
    @RequestParam(required = false) BigDecimal minRent,
    @RequestParam(required = false) BigDecimal maxRent,
    @RequestParam(required = false) String query
  )
  {
    ListedUnitsPageDTO response = unitService.getListedUnits(
     page,
     size,
     sortBy,
     direction,
     listed,
     buildingId,
     rentalProfileId,
     type,
     status,
     city,
     region,
     ward,
     streetName,
     country,
     minBedrooms,
     maxBedrooms,
     minBathrooms,
     maxBathrooms,
     minRent,
     maxRent,
     query
    );

    return ResponseEntity.ok(ApiResponse.success(response, HttpStatus.OK.value()));
  }

  @GetMapping("/nearby")
  public ResponseEntity<ApiResponse<ListedUnitsPageDTO>> getNearbyUnits(
    @RequestParam Double latitude,
    @RequestParam Double longitude,
    @RequestParam Double radiusKm,
    @RequestParam(required = false, defaultValue = "0") Integer page,
    @RequestParam(required = false, defaultValue = "20") Integer size,
    @RequestParam(required = false, defaultValue = "rentAmount") String sortBy,
    @RequestParam(required = false, defaultValue = "desc") String direction,
    @RequestParam(required = false) Boolean listed
  )
  {
    ListedUnitsPageDTO response = unitService.getNearbyUnits(
     page,
     size,
     sortBy,
     direction,
     listed,
     latitude,
     longitude,
     radiusKm
    );

    return ResponseEntity.ok(ApiResponse.success(response, HttpStatus.OK.value()));
  }

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

  @PatchMapping("/{unitId}/listed")
  public ResponseEntity<ApiResponse<Boolean>> updateUnitListing(@PathVariable Long unitId, @Valid @RequestBody UnitListingUpdateDTO request)
  {
    Boolean updatedListed = unitService.updateUnitListed(unitId, request.listed());
    return ResponseEntity.ok(ApiResponse.success(updatedListed, HttpStatus.OK.value()));
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
