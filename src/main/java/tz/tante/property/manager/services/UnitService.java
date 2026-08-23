package tz.tante.property.manager.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tz.tante.property.manager.exceptions.TanteException;
import tz.tante.property.manager.models.dtos.requests.UnitCreateDTO;
import tz.tante.property.manager.models.dtos.responses.UnitDetailsDTO;
import tz.tante.property.manager.models.entities.Unit;
import tz.tante.property.manager.repositories.UnitRepository;

@Service
@AllArgsConstructor
public class UnitService
{
  private final UnitRepository unitRepository;

  public UnitDetailsDTO createUnit(UnitCreateDTO request)
  {
    try
    {
      Unit unit = new Unit();
      unit.setUnitNumber(request.unitNumber());
      unit.setRentalProfileId(request.rentalProfileId());
      unit.setNumberOfBedrooms(request.numberOfBedrooms());
      unit.setNumberOfBathrooms(request.numberOfBathrooms());
      unit.setNumberParkingSpots(request.numberParkingSpots());
      unit.setRentAmount(request.rentAmount());
      unit.setType(request.type());
      unit.setStatus(request.status());
      unit.setSize(request.size());
      unit.setSizeUnit(request.sizeUnit());

      unit = unitRepository.save(unit);

      return mapToUnitDetailsDTO(unit);
    }
    catch (Exception exception)
    {
      throw new TanteException(exception.getMessage());
    }
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
      unit.getSize(),
      unit.getSizeUnit(),
      null);
  }
}
