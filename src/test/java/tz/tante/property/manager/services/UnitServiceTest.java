package tz.tante.property.manager.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import tz.tante.property.manager.enums.UnitStatus;
import tz.tante.property.manager.enums.UnitType;
import tz.tante.property.manager.exceptions.TanteException;
import tz.tante.property.manager.models.dtos.responses.PageDTO;
import tz.tante.property.manager.models.dtos.responses.UnitSummaryDTO;
import tz.tante.property.manager.models.entities.Unit;
import tz.tante.property.manager.models.entities.UnitImage;
import tz.tante.property.manager.repositories.BuildingRepository;
import tz.tante.property.manager.repositories.UnitImageRepository;
import tz.tante.property.manager.repositories.UnitRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

  private UnitService unitService;

  @BeforeEach
  void setUp()
  {
    unitService = new UnitService(unitRepository, buildingRepository, unitImageRepository, s3StorageService);
  }

  @Test
  void getFilteredUnits_rejectsInvalidBedroomRange()
  {
    assertThrows(TanteException.class, () ->
      unitService.getFilteredUnits(
        0,
        20,
        "rentAmount",
        "desc",
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
        10,
        5,
        null,
        null,
        null,
        null,
        null
      )
    );
  }

  @Test
  void getFilteredUnits_rejectsNegativeRentRange()
  {
    assertThrows(TanteException.class, () ->
      unitService.getFilteredUnits(
        0,
        20,
        "rentAmount",
        "desc",
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
        new BigDecimal("-1000"),
        new BigDecimal("1000"),
        null
      )
    );
  }

  @Test
  void getFilteredUnits_usesFallbackSortAndReturnsPageMetadata()
  {
    Page<Unit> result = new PageImpl<>(List.of(), org.springframework.data.domain.PageRequest.of(0, 20), 0);
    when(unitRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(result);

    PageDTO<UnitSummaryDTO> page = unitService.getFilteredUnits(
      0,
      20,
      "unexpected-sort",
      "up",
      true,
      null,
      null,
      UnitType.FLAT,
      UnitStatus.AVAILABLE,
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

    assertNotNull(page);
    assertEquals(0, page.page());
    assertEquals(20, page.size());
    assertFalse(page.hasNext());
    assertFalse(page.hasPrevious());
  }

  @Test
  void unitToUnitSummaryDTO_handlesEmptyImageList()
  {
    Unit unit = new Unit();
    unit.setId(42L);
    unit.setUnitNumber("U-42");
    unit.setNumberOfBedrooms(2);
    unit.setNumberOfBathrooms(1);
    unit.setNumberParkingSpots(1);
    unit.setRentAmount(new BigDecimal("2500.00"));
    unit.setType(UnitType.FLAT);
    unit.setStatus(UnitStatus.AVAILABLE);
    unit.setListed(true);
    unit.setImages(List.of());

    UnitSummaryDTO dto = tz.tante.property.manager.utilities.Utility.unitToUnitSummaryDTO(unit);

    assertNotNull(dto);
    assertNull(dto.coverImage());
    assertEquals("U-42", dto.unitNumber());
  }
}
