package tz.tante.property.manager.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tz.tante.property.manager.enums.UnitStatus;
import tz.tante.property.manager.exceptions.ResourceNotFoundException;
import tz.tante.property.manager.exceptions.TanteException;
import tz.tante.property.manager.models.dtos.requests.UnitCreateDTO;
import tz.tante.property.manager.models.dtos.responses.UnitDetailsDTO;
import tz.tante.property.manager.models.dtos.responses.UnitImageDTO;
import tz.tante.property.manager.models.entities.Building;
import tz.tante.property.manager.models.entities.Unit;
import tz.tante.property.manager.models.entities.UnitImage;
import tz.tante.property.manager.repositories.BuildingRepository;
import tz.tante.property.manager.repositories.UnitImageRepository;
import tz.tante.property.manager.repositories.UnitRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UnitService
{
  private final UnitRepository unitRepository;
  private final BuildingRepository buildingRepository;
  private final UnitImageRepository unitImageRepository;
  private final S3StorageService s3StorageService;

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

  @Transactional
  public UnitImageDTO uploadUnitImage(Long unitId, MultipartFile file, Long createdByUserId)
  {
    Unit unit = unitRepository.findById(unitId)
      .orElseThrow(() -> new ResourceNotFoundException("Unit with ID " + unitId + " not found"));

    S3StorageService.StoredImage storedImage = s3StorageService.uploadFile(file, "units/" + unitId);

    UnitImage image = new UnitImage();
    image.setObjectKey(storedImage.objectKey());
    image.setUrl(storedImage.url());
    image.setFileName(storedImage.fileName());
    image.setContentType(storedImage.contentType());
    image.setSize(storedImage.size());
    image.setCreatedByUserId(createdByUserId);
    image.setPrimaryImage(unit.getImages().isEmpty());
    unit.addImage(image);

    UnitImage savedImage = unitImageRepository.save(image);
    return mapToUnitImageDTO(savedImage);
  }

  @Transactional
  public List<UnitImageDTO> getUnitImages(Long unitId)
  {
    Unit unit = unitRepository.findById(unitId)
      .orElseThrow(() -> new ResourceNotFoundException("Unit with ID " + unitId + " not found"));

    return unit.getImages().stream()
      .map(this::mapToUnitImageDTO)
      .toList();
  }

  @Transactional
  public void deleteUnitImage(Long unitId, Long imageId)
  {
    Unit unit = unitRepository.findById(unitId)
      .orElseThrow(() -> new ResourceNotFoundException("Unit with ID " + unitId + " not found"));

    UnitImage image = unitImageRepository.findById(imageId)
      .orElseThrow(() -> new ResourceNotFoundException("Unit image with ID " + imageId + " not found"));

    if (!image.getUnit().getId().equals(unit.getId()))
    {
      throw new TanteException("Unit image does not belong to this unit");
    }

    unit.getImages().remove(image);
    s3StorageService.deleteFile(image.getObjectKey());
    unitImageRepository.delete(image);
  }

  private UnitDetailsDTO mapToUnitDetailsDTO(Unit unit)
  {
    List<UnitImageDTO> imageDTOs = unit.getImages() == null
      ? new ArrayList<>()
      : unit.getImages().stream().map(this::mapToUnitImageDTO).toList();

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
      unit.getBuilding() == null ? null : unit.getBuilding().getId(),
      imageDTOs);
  }

  private UnitImageDTO mapToUnitImageDTO(UnitImage image)
  {
    if (image == null)
    {
      return null;
    }

    return new UnitImageDTO(
      image.getId(),
      image.getUrl(),
      image.getFileName(),
      image.getContentType(),
      image.getSize(),
      image.isPrimaryImage()
    );
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
