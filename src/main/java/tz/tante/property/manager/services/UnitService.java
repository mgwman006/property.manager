package tz.tante.property.manager.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tz.tante.property.manager.enums.UnitStatus;
import tz.tante.property.manager.exceptions.ResourceNotFoundException;
import tz.tante.property.manager.models.dtos.requests.UnitCreateDTO;
import tz.tante.property.manager.models.dtos.responses.UnitDetailsDTO;
import tz.tante.property.manager.models.entities.Building;
import tz.tante.property.manager.models.entities.Unit;
import tz.tante.property.manager.repositories.BuildingRepository;
import tz.tante.property.manager.repositories.UnitRepository;

@Service
@AllArgsConstructor
public class UnitService
{
  private final UnitRepository unitRepository;
  private final BuildingRepository buildingRepository;

  @Transactional
  public UnitDetailsDTO createUnit(Long buildingId, UnitCreateDTO request)
  {
    Building building = buildingRepository.findById(buildingId)
      .orElseThrow(() -> new ResourceNotFoundException("Building with ID " + buildingId + " not found"));

    Unit unit = new Unit();
    unit.setUnitNumber(request.unitNumber());
    unit.setRentalProfileId(request.rentalProfileId());
    unit.setNumberOfBedrooms(request.numberOfBedrooms());
    unit.setNumberOfBathrooms(request.numberOfBathrooms());
    unit.setNumberParkingSpots(request.numberParkingSpots());
    unit.setRentAmount(request.rentAmount());
    unit.setType(request.type());
    unit.setStatus(UnitStatus.AVAILABLE);
    unit.setRoomSize(request.roomSize());
    unit.setSizeUnit(request.sizeUnit());

    building.addUnit(unit);
    unit = unitRepository.save(unit);

    return mapToUnitDetailsDTO(unit);
  }

  public UnitStatus getUnitStatus(Long unitId)
  {
    Unit unit = unitRepository.findById(unitId)
      .orElseThrow(() -> new ResourceNotFoundException("Unit with ID " + unitId + " not found"));
    return unit.getStatus();
  }

  private UnitDetailsDTO mapToUnitDetailsDTO(Unit unit)
  {
    return new UnitDetailsDTO(
      unit.getId(),
      unit.getUnitNumber(),
      unit.getRentalProfileId(),
      unit.getNumberOfBedrooms(),
      unit.getNumberOfBathrooms(),
      unit.getNumberParkingSpots(),
      unit.getRentAmount(),
      unit.getType(),
      unit.getStatus(),
      unit.getRoomSize(),
      unit.getSizeUnit(),
      null);
  }

  public UnitStatus updateUnitStatus(Long unitId, UnitStatus status)
  {
    Unit unit = unitRepository.findById(unitId)
      .orElseThrow(() -> new ResourceNotFoundException("Unit with ID " + unitId + " not found"));
    unit.setStatus(status);
    unit = unitRepository.save(unit);
    return unit.getStatus();
  }
}
