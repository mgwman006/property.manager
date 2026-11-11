package tz.tante.property.manager.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import tz.tante.property.manager.enums.PropertyRegister;
import tz.tante.property.manager.exceptions.ResourceNotFoundException;
import tz.tante.property.manager.exceptions.TanteException;
import tz.tante.property.manager.models.dtos.AddressDTO;
import tz.tante.property.manager.models.dtos.requests.PropertyCreateDTO;
import tz.tante.property.manager.models.dtos.requests.PropertyOwnerDTO;
import tz.tante.property.manager.models.dtos.responses.*;
import tz.tante.property.manager.models.entities.*;
import tz.tante.property.manager.repositories.PropertyImageRepository;
import tz.tante.property.manager.repositories.PropertyRepository;
import tz.tante.property.manager.repositories.PropertySequenceRepository;
import tz.tante.property.manager.utilities.Utility;

@Getter
@Setter
@Service
@AllArgsConstructor
public class PropertyService
{
  /**
   * Constants
   */
  private static final ZoneId UTC = ZoneId.of("UTC");
  private static final String PROPERTY_WITH_ID = "Property with id ";
  private static final String NOT_FOUND = " not found";

  private final PropertyRepository propertyRepository;
  private final PropertySequenceRepository propertySequenceRepository;
  private final PropertyImageRepository propertyImageRepository;
  private final S3StorageService s3StorageService;

  @Transactional
  public PropertyDetailsDTO registerPropertyByRentalProfileId(Long rentalProfileId, PropertyCreateDTO request)
  {
    if (rentalProfileId == null || rentalProfileId <= 0)
    {
      throw new TanteException("Rental profile ID is required and must be greater than 0");
    }

    int currentYear = LocalDateTime.now(UTC).getYear();
    Property property = new Property();
    property.setRentalProfileId(rentalProfileId);
    property.setRegisteredBy(PropertyRegister.RENTAL_PROFILE);
    property.setDescription(request.description());
    property.setCreatedByUserId(request.createdByUserId());
    property.setName(request.name());
    property.setLandSize(request.landSize());
    property.setLandSizeUnit(request.landSizeUnit());
    property.setType(request.type());
    property.setDevelopmentStatus(request.developmentStatus());

    Location location = mapToLocation(request);
    location.setCreatedByUserId(request.createdByUserId());
    location.setProperty(property);
    property.setLocation(location);

    PropertySequence propertySequence = propertySequenceRepository.findForUpdate(currentYear)
      .orElse(null);

    if (propertySequence == null)
    {
      propertySequence = createSequence(currentYear);
    }

    Long nextSequence = propertySequence.getLastSequence() + 1;
    propertySequence.setLastSequence(nextSequence);
    property.setCode(String.format("TNT-%d-%06d", currentYear, nextSequence));

    createBuildingsForProperty(property, request.numberOfBuildings());

    if (request.owners() != null && !request.owners().isEmpty())
    {
      List<PropertyOwner> owners = mapOwners(request, property);
      property.setOwners(owners);
    }

    Property savedProperty = propertyRepository.save(property);
    return Utility.propertyToPropertyDetailsDTO(savedProperty);
  }

  @Transactional
  public PropertyDetailsDTO getPropertyDetailsById(Long id)
  {
    Property property = propertyRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException(PROPERTY_WITH_ID + id + NOT_FOUND));

    return Utility.propertyToPropertyDetailsDTO(property);
  }

  @Transactional
  public PropertyImageDTO uploadPropertyImage(Long propertyId, MultipartFile file, Long createdByUserId)
  {
    Property property = propertyRepository.findById(propertyId)
      .orElseThrow(() -> new ResourceNotFoundException(PROPERTY_WITH_ID + propertyId + NOT_FOUND));

    S3StorageService.StoredImage storedImage = s3StorageService.uploadFile(file, property.getCode());

    PropertyImage image = new PropertyImage();
    image.setObjectKey(storedImage.objectKey());
    image.setUrl(storedImage.url());
    image.setFileName(storedImage.fileName());
    image.setContentType(storedImage.contentType());
    image.setSize(storedImage.size());
    image.setCreatedByUserId(createdByUserId);
    image.setPrimaryImage(property.getImages().isEmpty());
    property.addImage(image);

    PropertyImage savedImage = propertyImageRepository.save(image);
    return mapToPropertyImageDTO(savedImage);
  }

  @Transactional
  public List<PropertyImageDTO> getPropertyImages(Long propertyId)
  {
    Property property = propertyRepository.findById(propertyId)
      .orElseThrow(() -> new ResourceNotFoundException(PROPERTY_WITH_ID + propertyId + NOT_FOUND));

    return property.getImages().stream()
      .map(this::mapToPropertyImageDTO)
      .toList();
  }

  @Transactional
  public void deletePropertyImage(Long propertyId, Long imageId)
  {
    Property property = propertyRepository.findById(propertyId)
      .orElseThrow(() -> new ResourceNotFoundException(PROPERTY_WITH_ID + propertyId + NOT_FOUND));

    PropertyImage image = propertyImageRepository.findById(imageId)
      .orElseThrow(() -> new ResourceNotFoundException("Property image with id " + imageId + NOT_FOUND));

    if (!image.getProperty().getId().equals(property.getId()))
    {
      throw new TanteException("Property image does not belong to this property");
    }

    property.getImages().remove(image);
    s3StorageService.deleteFile(image.getObjectKey());
    propertyImageRepository.delete(image);
  }

  @Transactional
  public List<PropertyDetailsDTO> getAllProperties()
  {
    return propertyRepository.findAll()
      .stream()
      .map(Utility::propertyToPropertyDetailsDTO)
      .toList();
  }

  private void createBuildingsForProperty(Property property, int numberOfBuildings)
  {
    if (numberOfBuildings <= 0)
    {
      throw new TanteException("Number of buildings must be greater than 0");
    }

    for (int i = 1; i <= numberOfBuildings; i++)
    {
      String buildingName = "Building " + i;
      Building building = new Building();
      building.setName(buildingName);
      building.setCreatedByUserId(property.getCreatedByUserId());
      property.addBuilding(building);
    }
  }

  private Location mapToLocation(PropertyCreateDTO request)
  {
    Location location = new Location();
    location.setLatitude(request.location().latitude());
    location.setLongitude(request.location().longitude());
    location.setAddress(mapToAddress(request.location().address()));
    return location;
  }

  private Address mapToAddress(AddressDTO address)
  {
    if (address == null)
    {
      return null;
    }

    return new Address(
      address.postalCode(),
      address.streetNumber(),
      address.streetName(),
      address.ward(),
      address.city(),
      address.region(),
      address.country());
  }

  private List<PropertyOwner> mapOwners(PropertyCreateDTO request, Property property)
  {
    List<PropertyOwner> owners = new ArrayList<>();
    for (PropertyOwnerDTO ownerRequest : request.owners())
    {
      PropertyOwner owner = new PropertyOwner();
      owner.setOwnerType(ownerRequest.ownerType());
      owner.setOwnerId(ownerRequest.ownerId());
      owner.setOwnershipPercentage(ownerRequest.ownershipPercentage());
      owner.setCreatedByUserId(request.createdByUserId());
      owner.setProperty(property);
      owners.add(owner);
    }

    return owners;
  }



  private PropertyImageDTO mapToPropertyImageDTO(PropertyImage image)
  {
    if (image == null)
    {
      return null;
    }

    return new PropertyImageDTO(
      image.getId(),
      image.getUrl(),
      image.getFileName(),
      image.getContentType(),
      image.getSize(),
      image.isPrimaryImage()
    );
  }

  private PropertySequence createSequence(int year)
  {
    PropertySequence sequence = new PropertySequence();
    sequence.setYear(year);
    sequence.setLastSequence(0L);
    return propertySequenceRepository.save(sequence);
  }

  public List<PropertyDetailsDTO> getPropertiesByRentalProfileId(Long rentalProfileId)
  {
    List<Property> properties = propertyRepository.findByRentalProfileId(rentalProfileId);
    return properties.stream().map(Utility::propertyToPropertyDetailsDTO).toList();
  }
}
