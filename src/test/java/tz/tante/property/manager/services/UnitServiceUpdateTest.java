package tz.tante.property.manager.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tz.tante.property.manager.exceptions.ResourceNotFoundException;
import tz.tante.property.manager.models.entities.Unit;
import tz.tante.property.manager.repositories.BuildingRepository;
import tz.tante.property.manager.repositories.UnitImageRepository;
import tz.tante.property.manager.repositories.UnitRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnitServiceUpdateTest
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
  void shouldUpdateUnitListedStatus()
  {
    Unit unit = new Unit();
    unit.setId(9L);
    unit.setListed(false);

    when(unitRepository.findById(9L)).thenReturn(java.util.Optional.of(unit));
    when(unitRepository.save(unit)).thenReturn(unit);

    Boolean updated = unitService.updateUnitListingStatus(9L, true);

    assertTrue(updated);
    assertTrue(unit.isListed());
    verify(unitRepository).save(unit);
  }

  @Test
  void shouldThrowWhenUnitDoesNotExist()
  {
    when(unitRepository.findById(99L)).thenReturn(java.util.Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> unitService.updateUnitListingStatus(99L, true));
  }
}
