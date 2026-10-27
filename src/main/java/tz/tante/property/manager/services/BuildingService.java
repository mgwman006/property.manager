package tz.tante.property.manager.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tz.tante.property.manager.exceptions.ResourceNotFoundException;
import tz.tante.property.manager.models.dtos.requests.BuildingDetailsUpdateDTO;
import tz.tante.property.manager.models.dtos.responses.BuildingDetailsDTO;
import tz.tante.property.manager.models.dtos.responses.UnitDetailsDTO;
import tz.tante.property.manager.models.entities.Building;
import tz.tante.property.manager.repositories.BuildingRepository;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class BuildingService
{
  private final BuildingRepository buildingRepository;

  public BuildingDetailsDTO updateBuildingDetails(Long buildingId, BuildingDetailsUpdateDTO request)
  {
    Building building = buildingRepository.findById(buildingId)
      .orElseThrow(() -> new ResourceNotFoundException("Building with ID " + buildingId + " not found"));

    building.setName(request.name());
    building.setNumber(request.number());
    building.setCode(request.code());
    building.setDescription(request.description());
    building.setNumberOfFloors(request.numberOfFloors());
    building = buildingRepository.save(building);

    return mapToBuildingDetailsDTO(building);
  }

  private BuildingDetailsDTO mapToBuildingDetailsDTO(tz.tante.property.manager.models.entities.Building building)
  {
    return new BuildingDetailsDTO(
      building.getProperty().getId(),
      building.getId(),
      building.getName(),
      building.getNumber(),
      building.getCode(),
      building.getDescription(),
      building.getNumberOfFloors(),
      new ArrayList<UnitDetailsDTO>()
    );
  }
}
