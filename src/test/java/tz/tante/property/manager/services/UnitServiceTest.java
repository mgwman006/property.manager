package tz.tante.property.manager.services;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import tz.tante.property.manager.enums.UnitStatus;
import tz.tante.property.manager.enums.UnitType;
import tz.tante.property.manager.models.dtos.responses.ListedUnitsPageDTO;
import tz.tante.property.manager.models.entities.Building;
import tz.tante.property.manager.models.entities.Location;
import tz.tante.property.manager.models.entities.Property;
import tz.tante.property.manager.models.entities.Unit;
import tz.tante.property.manager.repositories.BuildingRepository;
import tz.tante.property.manager.repositories.UnitImageRepository;
import tz.tante.property.manager.repositories.UnitRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UnitServiceTest
{
  @Mock
  private UnitRepository unitRepository;

  @Mock
  private BuildingRepository buildingRepository;

  @Mock
  private UnitImageRepository unitImageRepository;

  @Mock
  private S3StorageService s3StorageService;

  @InjectMocks
  private UnitService unitService;

  @Test
  void shouldReturnListedUnitsWithPaginationMetadata()
  {
    Unit unit = new Unit();
    unit.setId(1L);
    unit.setListed(true);
    unit.setUnitNumber("A-101");
    unit.setRentalProfileId(99L);
    unit.setNumberOfBedrooms(2);
    unit.setNumberOfBathrooms(1);
    unit.setNumberParkingSpots(1);
    unit.setRentAmount(new BigDecimal("1500.00"));
    unit.setType(UnitType.FLAT);
    unit.setStatus(UnitStatus.AVAILABLE);
    unit.setRoomSize(45L);
    unit.setSizeUnit("m2");

    Page<Unit> unitsPage = new PageImpl<>(List.of(unit), PageRequest.of(0, 20), 1);
    when(unitRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(unitsPage);

    ListedUnitsPageDTO result = unitService.getListedUnits(
      0,
      20,
      "rentAmount",
      "desc",
      null,
      null,
      null,
      UnitType.FLAT,
      UnitStatus.AVAILABLE,
      "Dar es Salaam",
      "Dar es Salaam",
      "Kigamboni",
      "Nyerere Rd",
      "Tanzania",
      2,
      2,
      1,
      1,
      new BigDecimal("1200"),
      new BigDecimal("2000"),
      "A-101"
    );

    assertNotNull(result);
    assertEquals(1, result.items().size());
    assertEquals(1, result.totalPages());
    assertEquals(1L, result.totalElements());
    assertEquals("A-101", result.items().getFirst().unitNumber());
    verify(unitRepository).findAll(any(Specification.class), any(Pageable.class));
  }

  @Test
  void shouldIncludeUnlistedUnitsWhenListingFilterIsNotProvided()
  {
    Unit unlistedUnit = new Unit();
    unlistedUnit.setId(3L);
    unlistedUnit.setListed(false);
    unlistedUnit.setUnitNumber("3");
    unlistedUnit.setRentalProfileId(2L);
    unlistedUnit.setRentAmount(new BigDecimal("100000.00"));
    unlistedUnit.setType(UnitType.FLAT);
    unlistedUnit.setRoomSize(299L);
    unlistedUnit.setSizeUnit("square meter");

    Page<Unit> unitsPage = new PageImpl<>(List.of(unlistedUnit), PageRequest.of(0, 12), 1);
    when(unitRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(unitsPage);

    ListedUnitsPageDTO result = unitService.getListedUnits(
      0,
      12,
      "rentAmount",
      "asc",
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null
    );

    assertEquals(1, result.items().size());
    assertEquals("3", result.items().getFirst().unitNumber());

    ArgumentCaptor<Specification<Unit>> specification = ArgumentCaptor.forClass(Specification.class);
    verify(unitRepository).findAll(specification.capture(), any(Pageable.class));
    Root<Unit> root = mock();
    CriteriaQuery<?> criteriaQuery = mock();
    CriteriaBuilder criteriaBuilder = mock();
    specification.getValue().toPredicate(root, criteriaQuery, criteriaBuilder);
    verify(root, never()).get("listed");
  }

  @Test
  void shouldReturnNearbyUnitsWithinRadiusWithPaginationMetadata()
  {
    Unit unit = new Unit();
    unit.setId(2L);
    unit.setListed(true);
    unit.setUnitNumber("B-202");
    unit.setRentalProfileId(77L);
    unit.setNumberOfBedrooms(3);
    unit.setNumberOfBathrooms(2);
    unit.setNumberParkingSpots(2);
    unit.setRentAmount(new BigDecimal("2500.00"));
    unit.setType(UnitType.HOUSE);
    unit.setStatus(UnitStatus.AVAILABLE);
    unit.setRoomSize(75L);
    unit.setSizeUnit("m2");

    Location location = new Location();
    location.setLatitude(-6.8001);
    location.setLongitude(39.2001);
    Property property = new Property();
    property.setLocation(location);
    location.setProperty(property);
    Building building = new Building();
    building.setProperty(property);
    property.setBuildings(List.of(building));
    unit.setBuilding(building);

    Page<Unit> unitsPage = new PageImpl<>(List.of(unit), PageRequest.of(0, 20), 1);
    when(unitRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(unitsPage);

    ListedUnitsPageDTO result = unitService.getNearbyUnits(
      0,
      20,
      "rentAmount",
      "desc",
      true,
      -6.8,
      39.2,
      5.0
    );

    assertNotNull(result);
    assertEquals(1, result.items().size());
    assertEquals("B-202", result.items().getFirst().unitNumber());
  }
}
