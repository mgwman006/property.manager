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

import tz.tante.property.manager.exceptions.ResourceNotFoundException;
import tz.tante.property.manager.exceptions.TanteException;
import tz.tante.property.manager.models.dtos.AddressDTO;
import tz.tante.property.manager.models.dtos.requests.PropertyCreateDTO;
import tz.tante.property.manager.models.dtos.requests.PropertyOwnerDTO;
import tz.tante.property.manager.models.dtos.responses.PropertyDetailsDTO;
import tz.tante.property.manager.models.entities.Address;
import tz.tante.property.manager.models.entities.Location;
import tz.tante.property.manager.models.entities.Property;
import tz.tante.property.manager.models.entities.PropertyOwner;
import tz.tante.property.manager.models.entities.PropertySequence;
import tz.tante.property.manager.repositories.PropertyRepository;
import tz.tante.property.manager.repositories.PropertySequenceRepository;

@Getter
@Setter
@Service
@AllArgsConstructor
public class PropertyService
{
  private static final ZoneId UTC = ZoneId.of("UTC");

  private final PropertyRepository propertyRepository;
  private final PropertySequenceRepository propertySequenceRepository;

  @Transactional
  public PropertyDetailsDTO registerProperty(PropertyCreateDTO request)
  {
    if (request.location() == null)
    {
      throw new TanteException("Property location is required");
    }

    if (request.owners() == null)
    {
      throw new TanteException("Property owners are required");
    }

    int currentYear = LocalDateTime.now(UTC).getYear();
    Property property = new Property();
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
    property.setOwners(mapOwners(request, property));

    PropertySequence propertySequence = propertySequenceRepository.findForUpdate(currentYear)
      .orElseGet(() -> createSequence(currentYear));

    Long nextSequence = propertySequence.getLastSequence() + 1;
    propertySequence.setLastSequence(nextSequence);
    property.setCode(String.format("TNT-%d-%d", currentYear, nextSequence));

    Property savedProperty = propertyRepository.save(property);
    return mapToPropertyDetailsDTO(savedProperty);
  }

  @Transactional
  public PropertyDetailsDTO getPropertyDetailsById(Long id)
  {
    Property property = propertyRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Property with id " + id + " not found"));

    return mapToPropertyDetailsDTO(property);
  }

  @Transactional
  public List<PropertyDetailsDTO> getAllProperties()
  {
    return propertyRepository.findAll().stream().map(this::mapToPropertyDetailsDTO).toList();
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
      address.country(),
      address.popularAreaName() == null ? address.area() : address.popularAreaName()
    );
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

  private PropertyDetailsDTO mapToPropertyDetailsDTO(Property property)
  {
    return new PropertyDetailsDTO(
      property.getId(),
      property.getCode(),
      property.getDescription(),
      property.getName(),
      property.getLandSize(),
      property.getLandSizeUnit(),
      property.getType() == null ? null : property.getType().toString(),
      property.getDevelopmentStatus() == null ? null : property.getDevelopmentStatus().toString()
    );
  }

  private PropertySequence createSequence(int year)
  {
    PropertySequence sequence = new PropertySequence();
    sequence.setYear(year);
    sequence.setLastSequence(0L);
    return propertySequenceRepository.save(sequence);
  }
}
