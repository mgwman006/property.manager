package tz.tante.property.manager.services;


import tz.tante.property.manager.enums.UnitStatus;
import tz.tante.property.manager.enums.UnitType;
import tz.tante.property.manager.exceptions.ResourceNotFoundException;
import tz.tante.property.manager.exceptions.TanteException;
import tz.tante.property.manager.models.dtos.requests.UnitCreateDTO;
import tz.tante.property.manager.models.dtos.responses.*;
import tz.tante.property.manager.models.entities.Building;
import tz.tante.property.manager.models.entities.Unit;
import tz.tante.property.manager.models.entities.UnitImage;
import tz.tante.property.manager.repositories.BuildingRepository;
import tz.tante.property.manager.repositories.UnitImageRepository;
import tz.tante.property.manager.repositories.UnitRepository;
import tz.tante.property.manager.utilities.Utility;

import java.math.BigDecimal;
import java.util.List;
import jakarta.persistence.criteria.JoinType;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class UnitService
{
  /**
   * CONSTANTS
   * ---------
   */
  private static final String CLASS_NAME = UnitService.class.getSimpleName();
  private static final String NOT_FOUND = " not found";
  /**
   * FIELDS
   * ------
   */
  private final UnitRepository unitRepository;
  private final BuildingRepository buildingRepository;
  private final UnitImageRepository unitImageRepository;
  private final S3StorageService s3StorageService;

  @Transactional(readOnly = true)
  public PageDTO<UnitSummaryDTO> getFilteredUnits(
    Integer page,
    Integer size,
    String sortBy,
    String direction,
    Boolean isListed,
    Long buildingId,
    Long rentalProfileId,
    UnitType type,
    UnitStatus status,
    String city,
    String region,
    String ward,
    String streetName,
    String country,
    Integer minBedrooms,
    Integer maxBedrooms,
    Integer minBathrooms,
    Integer maxBathrooms,
    BigDecimal minRent,
    BigDecimal maxRent,
    String query
  )
  {
    Pageable pageable = initializePageable(page, size, sortBy, direction);

    Specification<Unit> specification = (root, criteriaQuery, criteriaBuilder) ->
      criteriaBuilder.conjunction();

    if (isListed != null)
    {
      specification = specification.and((root, criteriaQuery, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("isListed"), isListed)
      );
    }

    if (buildingId != null)
    {
      specification = specification.and((root, criteriaQuery, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("building").get("id"), buildingId)
      );
    }

    if (rentalProfileId != null)
    {
      specification = specification.and((root, criteriaQuery, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("rentalProfileId"), rentalProfileId)
      );
    }

    if (type != null)
    {
      specification = specification.and((root, criteriaQuery, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("type"), type)
      );
    }

    if (status != null)
    {
      specification = specification.and((root, criteriaQuery, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("status"), status)
      );
    }

    if (city != null && !city.isBlank())
    {
      String cityFilter = city.trim();
      specification = specification.and((root, criteriaQuery, criteriaBuilder) -> {
        var buildingJoin = root.join("building", JoinType.LEFT);
        var propertyJoin = buildingJoin.join("property", JoinType.LEFT);
        var locationJoin = propertyJoin.join("location", JoinType.LEFT);
        return criteriaBuilder.like(criteriaBuilder.lower(locationJoin.get("address").get("city")), "%" + cityFilter.toLowerCase() + "%");
      });
    }

    if (region != null && !region.isBlank())
    {
      String regionFilter = region.trim();
      specification = specification.and((root, criteriaQuery, criteriaBuilder) -> {
        var buildingJoin = root.join("building", JoinType.LEFT);
        var propertyJoin = buildingJoin.join("property", JoinType.LEFT);
        var locationJoin = propertyJoin.join("location", JoinType.LEFT);
        return criteriaBuilder.like(criteriaBuilder.lower(locationJoin.get("address").get("region")), "%" + regionFilter.toLowerCase() + "%");
      });
    }

    if (ward != null && !ward.isBlank())
    {
      String wardFilter = ward.trim();
      specification = specification.and((root, criteriaQuery, criteriaBuilder) -> {
        var buildingJoin = root.join("building", JoinType.LEFT);
        var propertyJoin = buildingJoin.join("property", JoinType.LEFT);
        var locationJoin = propertyJoin.join("location", JoinType.LEFT);
        return criteriaBuilder.like(criteriaBuilder.lower(locationJoin.get("address").get("ward")), "%" + wardFilter.toLowerCase() + "%");
      });
    }

    if (streetName != null && !streetName.isBlank())
    {
      String streetNameFilter = streetName.trim();
      specification = specification.and((root, criteriaQuery, criteriaBuilder) -> {
        var buildingJoin = root.join("building", JoinType.LEFT);
        var propertyJoin = buildingJoin.join("property", JoinType.LEFT);
        var locationJoin = propertyJoin.join("location", JoinType.LEFT);
        return criteriaBuilder.like(criteriaBuilder.lower(locationJoin.get("address").get("streetName")), "%" + streetNameFilter.toLowerCase() + "%");
      });
    }

    if (country != null && !country.isBlank())
    {
      String countryFilter = country.trim();
      specification = specification.and((root, criteriaQuery, criteriaBuilder) -> {
        var buildingJoin = root.join("building", JoinType.LEFT);
        var propertyJoin = buildingJoin.join("property", JoinType.LEFT);
        var locationJoin = propertyJoin.join("location", JoinType.LEFT);
        return criteriaBuilder.like(criteriaBuilder.lower(locationJoin.get("address").get("country")), "%" + countryFilter.toLowerCase() + "%");
      });
    }

    if (minBedrooms != null)
    {
      specification = specification.and((root, criteriaQuery, criteriaBuilder) ->
        criteriaBuilder.greaterThanOrEqualTo(root.get("numberOfBedrooms"), minBedrooms)
      );
    }

    if (maxBedrooms != null)
    {
      specification = specification.and((root, criteriaQuery, criteriaBuilder) ->
        criteriaBuilder.lessThanOrEqualTo(root.get("numberOfBedrooms"), maxBedrooms)
      );
    }

    if (minBathrooms != null)
    {
      specification = specification.and((root, criteriaQuery, criteriaBuilder) ->
        criteriaBuilder.greaterThanOrEqualTo(root.get("numberOfBathrooms"), minBathrooms)
      );
    }

    if (maxBathrooms != null)
    {
      specification = specification.and((root, criteriaQuery, criteriaBuilder) ->
        criteriaBuilder.lessThanOrEqualTo(root.get("numberOfBathrooms"), maxBathrooms)
      );
    }

    if (minRent != null)
    {
      specification = specification.and((root, criteriaQuery, criteriaBuilder) ->
        criteriaBuilder.greaterThanOrEqualTo(root.get("rentAmount"), minRent)
      );
    }

    if (maxRent != null)
    {
      specification = specification.and((root, criteriaQuery, criteriaBuilder) ->
        criteriaBuilder.lessThanOrEqualTo(root.get("rentAmount"), maxRent)
      );
    }

    if (query != null && !query.isBlank())
    {
      String normalized = query.trim();
      String lowerQuery = normalized.toLowerCase();
      specification = specification.and((root, criteriaQuery, criteriaBuilder) -> {
        var buildingJoin = root.join("building", JoinType.LEFT);
        var propertyJoin = buildingJoin.join("property", JoinType.LEFT);
        var locationJoin = propertyJoin.join("location", JoinType.LEFT);
        return criteriaBuilder.or(
          criteriaBuilder.like(criteriaBuilder.lower(root.get("unitNumber")), "%" + lowerQuery + "%"),
          criteriaBuilder.like(criteriaBuilder.lower(buildingJoin.get("code")), "%" + lowerQuery + "%"),
          criteriaBuilder.like(criteriaBuilder.lower(buildingJoin.get("name")), "%" + lowerQuery + "%"),
          criteriaBuilder.like(criteriaBuilder.lower(propertyJoin.get("name")), "%" + lowerQuery + "%"),
          criteriaBuilder.like(criteriaBuilder.lower(locationJoin.get("address").get("city")), "%" + lowerQuery + "%"),
          criteriaBuilder.like(criteriaBuilder.lower(locationJoin.get("address").get("region")), "%" + lowerQuery + "%"),
          criteriaBuilder.like(criteriaBuilder.lower(locationJoin.get("address").get("ward")), "%" + lowerQuery + "%"),
          criteriaBuilder.like(criteriaBuilder.lower(locationJoin.get("address").get("streetName")), "%" + lowerQuery + "%"),
          criteriaBuilder.like(criteriaBuilder.lower(locationJoin.get("address").get("country")), "%" + lowerQuery + "%")
        );
      });
    }

    Page<Unit> result = unitRepository.findAll(specification, pageable);
    return new PageDTO<>(
      result.getContent().stream().map(Utility::unitToUnitSummaryDTO).toList(),
      result.getNumber(),
      result.getSize(),
      result.getTotalElements(),
      result.getTotalPages(),
      result.hasNext(),
      result.hasPrevious()
    );
  }

  private Pageable initializePageable(Integer page, Integer size, String sortBy, String direction)
  {
    int safePage = page == null ? 0 : Math.max(page, 0);
    int safeSize = size == null ? 20 : Math.max(Math.min(size, 100), 1);
    String safeSortBy = resolveSortField(sortBy);
    Sort.Direction sortDirection = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
    return PageRequest.of(safePage, safeSize, Sort.by(sortDirection, safeSortBy));
  }

  @Transactional(readOnly = true)
  public PageDTO<UnitSummaryDTO> getNearbyUnitsByCoordinates(
    Integer page,
    Integer size,
    String sortBy,
    String direction,
    Boolean isListed,
    Double latitude,
    Double longitude,
    Double radiusKm
  )
  {
    if (latitude == null || longitude == null || radiusKm == null)
    {
      throw new TanteException("latitude, longitude, and radiusKm are required for nearby unit search");
    }

    if (latitude < -90 || latitude > 90)
    {
      throw new TanteException("latitude must be between -90 and 90");
    }

    if (longitude < -180 || longitude > 180)
    {
      throw new TanteException("longitude must be between -180 and 180");
    }

    if (radiusKm <= 0)
    {
      throw new TanteException("radiusKm must be greater than 0");
    }

    Pageable pageable = initializePageable(page, size, sortBy, direction);

    boolean safeListed = isListed == null || isListed;
    Specification<Unit> specification = Specification.where((root, criteriaQuery, criteriaBuilder) ->
      criteriaBuilder.equal(root.get("isListed"), safeListed)
    );

    double latDelta = radiusKm / 111.0;
    double lngDelta = radiusKm / (111.0 * Math.cos(Math.toRadians(latitude)));

    specification = specification.and((root, criteriaQuery, criteriaBuilder) -> {
      var buildingJoin = root.join("building", JoinType.LEFT);
      var propertyJoin = buildingJoin.join("property", JoinType.LEFT);
      var locationJoin = propertyJoin.join("location", JoinType.LEFT);

      return criteriaBuilder.and(
        criteriaBuilder.greaterThanOrEqualTo(locationJoin.get("latitude"), latitude - latDelta),
        criteriaBuilder.lessThanOrEqualTo(locationJoin.get("latitude"), latitude + latDelta),
        criteriaBuilder.greaterThanOrEqualTo(locationJoin.get("longitude"), longitude - lngDelta),
        criteriaBuilder.lessThanOrEqualTo(locationJoin.get("longitude"), longitude + lngDelta)
      );
    });

    Page<Unit> result = unitRepository.findAll(specification, pageable);
    List<Unit> filteredResults = result.getContent().stream()
      .filter(unit -> unit.getBuilding() != null && unit.getBuilding().getProperty() != null && unit.getBuilding().getProperty().getLocation() != null)
      .filter(unit -> haversineDistanceKm(
        unit.getBuilding().getProperty().getLocation().getLatitude(),
        unit.getBuilding().getProperty().getLocation().getLongitude(),
        latitude,
        longitude
      ) <= radiusKm)
      .toList();

    long totalElements = filteredResults.size();
    int totalPages = filteredResults.isEmpty() ? 1 : (int) Math.ceil((double) totalElements / pageable.getPageSize());
    boolean hasNext = pageable.getPageNumber() + 1 < totalPages;
    boolean hasPrevious = pageable.getPageNumber() > 0;

    return new PageDTO<>(
      filteredResults.stream().map(Utility::unitToUnitSummaryDTO).toList(),
      pageable.getPageNumber(),
      pageable.getPageSize(),
      totalElements,
      totalPages,
      hasNext,
      hasPrevious
    );
  }

  @Transactional
  public UnitSummaryDTO createUnit(Long buildingId, UnitCreateDTO request)
  {
    Building building = buildingRepository.findById(buildingId)
      .orElseThrow(() -> new ResourceNotFoundException("Building with ID " + buildingId + NOT_FOUND));

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

    return Utility.unitToUnitSummaryDTO(unit);
  }

  public UnitStatus getUnitStatus(Long unitId)
  {
    Unit unit = unitRepository.findById(unitId)
      .orElseThrow(() -> new ResourceNotFoundException("Unit with ID " + unitId + NOT_FOUND));
    return unit.getStatus();
  }

  @Transactional(readOnly = true)
  public UnitDetailsDTO getUnitDetails(Long unitId)
  {
    Unit unit = unitRepository.findById(unitId)
      .orElseThrow(() -> new ResourceNotFoundException("Unit with ID " + unitId + NOT_FOUND));

    if (!unit.isListed())
    {
      throw new ResourceNotFoundException("Listed unit with ID " + unitId + NOT_FOUND);
    }

    return mapToUnitDetailsDTO(unit);
  }

  @Transactional
  public UnitImageDTO uploadUnitImage(Long unitId, MultipartFile file, Long createdByUserId)
  {
    Unit unit = unitRepository.findById(unitId)
      .orElseThrow(() -> new ResourceNotFoundException("Unit with ID " + unitId + NOT_FOUND));

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
      .orElseThrow(() -> new ResourceNotFoundException("Unit with ID " + unitId + NOT_FOUND));

    return unit.getImages().stream()
      .map(this::mapToUnitImageDTO)
      .toList();
  }

  @Transactional
  public void deleteUnitImage(Long unitId, Long imageId)
  {
    Unit unit = unitRepository.findById(unitId)
      .orElseThrow(() -> new ResourceNotFoundException("Unit with ID " + unitId + NOT_FOUND));

    UnitImage image = unitImageRepository.findById(imageId)
      .orElseThrow(() -> new ResourceNotFoundException("Unit image with ID " + imageId + NOT_FOUND));

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
    List<UnitImageDTO> imageDTOs = unit.getImages().stream().map(Utility::imageToUnitImageDTO).toList();

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
      unit.isListed(),
      unit.getFloorNumber(),
      Utility.buildingToBuildingSummaryDTO(unit.getBuilding()),
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
      .orElseThrow(() -> new ResourceNotFoundException("Unit with ID " + unitId + NOT_FOUND));
    unit.setStatus(status);
    unit = unitRepository.save(unit);
    return unit.getStatus();
  }

  @Transactional
  public Boolean updateUnitListingStatus(Long unitId, Boolean isListed)
  {
    Unit unit = unitRepository.findById(unitId)
      .orElseThrow(() -> new ResourceNotFoundException("Unit with ID " + unitId + NOT_FOUND));
    unit.setListed(isListed);
    unit = unitRepository.save(unit);
    return unit.isListed();
  }

  private double haversineDistanceKm(double lat1, double lon1, double lat2, double lon2)
  {
    double earthRadiusKm = 6371.0;
    double deltaLat = Math.toRadians(lat2 - lat1);
    double deltaLon = Math.toRadians(lon2 - lon1);
    double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
      + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
      * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return earthRadiusKm * c;
  }

  private String resolveSortField(String sortBy)
  {
    if (sortBy == null || sortBy.isBlank())
    {
      return "rentAmount";
    }

    return switch (sortBy.trim())
    {
      case "unitNumber" -> "unitNumber";
      case "numberOfBedrooms" -> "numberOfBedrooms";
      case "numberOfBathrooms" -> "numberOfBathrooms";
      case "rentAmount" -> "rentAmount";
      case "status" -> "status";
      case "type" -> "type";
      case "createdAt" -> "createdAt";
      case "updatedAt" -> "updatedAt";
      default -> "rentAmount";
    };
  }
}
