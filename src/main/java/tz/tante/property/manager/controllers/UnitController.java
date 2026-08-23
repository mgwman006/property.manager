package tz.tante.property.manager.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

  @PostMapping
  public ResponseEntity<ApiResponse<UnitDetailsDTO>> createUnit(@Valid @RequestBody UnitCreateDTO request)
  {
    UnitDetailsDTO response = unitService.createUnit(request);
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(ApiResponse.success(response, HttpStatus.CREATED.value()));
  }

}
