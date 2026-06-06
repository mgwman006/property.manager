package tz.tante.property.manager.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import org.springframework.stereotype.Service;

import tz.tante.property.manager.exceptions.TanteException;
import tz.tante.property.manager.models.dtos.AddressDTO;
import tz.tante.property.manager.models.dtos.requests.PropertyCreateDTO;
import tz.tante.property.manager.models.dtos.responses.PropertyDetailsDTO;
import tz.tante.property.manager.models.entities.Address;
import tz.tante.property.manager.models.entities.Property;
import tz.tante.property.manager.repositories.PropertyRepository;
import tz.tante.property.manager.utilities.Constant;
import tz.tante.property.manager.utilities.Utility;

@Getter
@Setter
@Service
@AllArgsConstructor
public class PropertyService
{
  private final PropertyRepository propertyRepository;

  @Transactional
  public PropertyDetailsDTO registerProperty(PropertyCreateDTO request)
  {
    try
    {
      Property property = new Property(
        request.description(),
        request.managingOrganizationId(),
        request.creatorId(),
        request.name(),
        "",
        request.landSize(),
        request.landSizeUnit(),
        request.latitude(),
        request.longitude(),
        new Address(
          request.address().street(),
          request.address().area(),
          request.address().city(),
          request.address().region(),
          request.address().country()
        ),
        request.type(),
        request.developmentStatus(),
        request.status()
      );

      property = propertyRepository.save(property);

      property.setCode(Utility.generateCode(Constant.PROPERTY_CODE_PREFIX, property.getId()));
      property = propertyRepository.save(property);

      return new PropertyDetailsDTO(
        property.getId(),
        property.getDescription(),
        property.getManagingOrganizationId(),
        property.getCreatorId(),
        property.getName(),
        property.getLandSize(),
        property.getLandSizeUnit(),
        property.getLatitude(),
        property.getLongitude(),
        new AddressDTO(
          property.getAddress().getStreet(),
          property.getAddress().getArea(),
          property.getAddress().getCity(),
          property.getAddress().getRegion(),
          property.getAddress().getCountry()
        ),
        property.getType().toString(),
        property.getDevelopmentStatus().toString(),
        property.getStatus().toString()
      );
    }
    catch (Exception exception)
    {
      throw new TanteException(exception.getMessage());
    }
  }
}
