package tz.tante.property.manager.utilities;

import tz.tante.property.manager.models.dtos.AddressDTO;
import tz.tante.property.manager.models.dtos.responses.LocationDetailsDTO;
import tz.tante.property.manager.models.dtos.responses.UnitImageDTO;
import tz.tante.property.manager.models.dtos.responses.UnitSummaryDTO;
import tz.tante.property.manager.models.entities.Address;
import tz.tante.property.manager.models.entities.Location;
import tz.tante.property.manager.models.entities.Unit;
import tz.tante.property.manager.models.entities.UnitImage;

public class Utility
{
  public static String generateCode(String prefix, Long sequence)
  {
    return String.format("%s-%06d", prefix, sequence);
  }

  public static UnitSummaryDTO unitToUnitSummaryDTO(Unit unit)
  {
    return new UnitSummaryDTO(
        unit.getId(),
        unit.getUnitNumber(),
        unit.getNumberOfBedrooms(),
        unit.getNumberOfBathrooms(),
        unit.getNumberParkingSpots(),
        unit.getRentAmount(),
        unit.getType(),
        unit.getStatus(),
        unit.getRoomSize(),
        unit.getSizeUnit(),
        unit.isListed(),
        unit.getBuilding() == null ? null : unit.getBuilding().getId(),
        unit.getImages().stream().map(Utility::imageToUnitImageDTO).toList().getFirst()
      );
  }

  public static UnitImageDTO imageToUnitImageDTO(UnitImage image)
  {
    UnitImageDTO unitImageDTO = null;
    if (image != null)
    {
      unitImageDTO = new UnitImageDTO(
        image.getId(),
        image.getUrl(),
        image.getFileName(),
        image.getContentType(),
        image.getSize(),
        image.isPrimaryImage()
      );
    }
    return unitImageDTO;
  }

  public static LocationDetailsDTO locationToLocationDetailsDTO(Location location)
  {
    LocationDetailsDTO locationDetailsDTO = null;
    if (location != null)
    {
      locationDetailsDTO = new LocationDetailsDTO(
        location.getLatitude(),
        location.getLongitude(),
        addressToAddressDTO(location.getAddress())
      );
    }
    return locationDetailsDTO;
  }

  public static AddressDTO addressToAddressDTO(Address address)
  {
    AddressDTO addressDTO = null;
    if (address != null)
    {
      addressDTO = new AddressDTO(
        address.getPostalCode(),
        address.getStreetNumber(),
        address.getStreetName(),
        address.getWard(),
        address.getCity(),
        address.getRegion(),
        address.getCountry()
      );
    }
    return addressDTO;
  }
}
