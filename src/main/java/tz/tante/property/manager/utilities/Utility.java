package tz.tante.property.manager.utilities;

import tz.tante.property.manager.models.dtos.AddressDTO;
import tz.tante.property.manager.models.dtos.responses.*;
import tz.tante.property.manager.models.entities.*;

import java.util.List;

public class Utility
{
  public static String generateCode(String prefix, Long sequence)
  {
    return String.format("%s-%06d", prefix, sequence);
  }

  public static UnitSummaryDTO unitToUnitSummaryDTO(Unit unit)
  {
    List<UnitImageDTO> coverImage = unit.getImages().stream()
      .map(Utility::imageToUnitImageDTO)
      .toList();

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
      unit.getFloorNumber(),
      unit.getBuilding() == null ? null : unit.getBuilding().getId(),
      unit.getBuilding().getProperty().getName(),
      locationToLocationDetailsDTO(unit.getBuilding().getProperty().getLocation()),
      coverImage
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

  public static PropertyImageDTO imageToPropertyImageDTO(PropertyImage image)
  {
    PropertyImageDTO propertyImageDTO = null;
    if (image != null)
    {
      propertyImageDTO = new PropertyImageDTO(
        image.getId(),
        image.getUrl(),
        image.getFileName(),
        image.getContentType(),
        image.getSize(),
        image.isPrimaryImage()
      );
    }
    return propertyImageDTO;
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
        address.getHouseNumber(),
        address.getStreetName(),
        address.getWard(),
        address.getCity(),
        address.getRegion(),
        address.getCountry()
      );
    }
    return addressDTO;
  }

  public static PropertyDetailsDTO propertyToPropertyDetailsDTO(Property property)
  {
    PropertyDetailsDTO propertyDetailsDTO = null;
    if (property != null)
    {
      propertyDetailsDTO = new PropertyDetailsDTO(
        property.getId(),
        property.getCode(),
        property.getDescription(),
        property.getName(),
        property.getLandSize(),
        property.getLandSizeUnit(),
        property.getType(),
        property.getDevelopmentStatus(),
        locationToLocationDetailsDTO(property.getLocation()),
        property.getBuildings().stream().map(Utility::buildingToBuildingDetailsDTO).toList(),
        property.getImages().stream().map(Utility::imageToPropertyImageDTO).toList()
      );
    }
    return propertyDetailsDTO;
  }

  public static BuildingDetailsDTO buildingToBuildingDetailsDTO(Building building)
  {
    BuildingDetailsDTO buildingDetailsDTO = null;
    if (building != null)
    {
      buildingDetailsDTO = new BuildingDetailsDTO(
        building.getId(),
        building.getName(),
        building.getNumber(),
        building.getCode(),
        building.getDescription(),
        building.getNumberOfFloors(),
        propertyToPropertySummaryDTO(building.getProperty()),
        building.getUnits().stream().map(Utility::unitToUnitSummaryDTO).toList()
      );
    }
    return buildingDetailsDTO;
  }

  public static BuildingSummaryDTO buildingToBuildingSummaryDTO(Building building)
  {
    BuildingSummaryDTO buildingSummaryDTO = null;
    if (building != null)
    {
      buildingSummaryDTO = new BuildingSummaryDTO(
        building.getId(),
        building.getName(),
        building.getNumber(),
        building.getCode(),
        building.getDescription(),
        building.getNumberOfFloors(),
        propertyToPropertySummaryDTO(building.getProperty())
      );
    }
    return buildingSummaryDTO;
  }

  public static PropertySummaryDTO propertyToPropertySummaryDTO(Property property)
  {
    PropertySummaryDTO propertySummaryDTO = null;
    if (property != null)
    {
      propertySummaryDTO = new PropertySummaryDTO(
        property.getId(),
        property.getName(),
        property.getCode(),
        property.getDescription(),
        property.getLandSize(),
        property.getLandSizeUnit(),
        locationToLocationDetailsDTO(property.getLocation()),
        property.getType(),
        property.getDevelopmentStatus(),
        property.getImages().stream().map(Utility::imageToPropertyImageDTO).toList()
      );
    }
    return propertySummaryDTO;
  }
}
