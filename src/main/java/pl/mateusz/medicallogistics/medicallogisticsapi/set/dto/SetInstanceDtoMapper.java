package pl.mateusz.medicallogistics.medicallogisticsapi.set.dto;

import pl.mateusz.medicallogistics.medicallogisticsapi.set.SetStatus;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.SetType;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetBase;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetInstance;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.domain.Location;

/**
 * Mapper class for converting between SetInstanceDto and SetInstance entities.
 * This class provides methods to map data from the DTO to the entity, ensuring
 * that the necessary transformations and validations are applied during the mapping process.
 */
public class SetInstanceDtoMapper {

  /**
   * Maps a SetInstanceDto to a SetInstance entity.
   *
   * @param dto the SetInstanceDto containing the data to be mapped
   * @param setBase the SetBase entity associated with the SetInstance
   * @param location the Location entity associated with the SetInstance
   * @return a SetInstance entity populated with data from the DTO
   */
  public static SetInstance mapToEntity(SetInstanceDto dto, SetBase setBase,
                                        Location location) {
    if (dto == null) {
      return null;
    }
    SetInstance setInstanceEntity = new SetInstance();
    setInstanceEntity.setTagId(dto.getSetTagId());
    setInstanceEntity.setSetType(SetType.valueOf(dto.getSetType()));
    setInstanceEntity.setSetBase(setBase);
    setInstanceEntity.setLocation(location);
    setInstanceEntity.setSetStatus(SetStatus.valueOf(dto.getSetStatus()));
    setInstanceEntity.setActive(dto.isActive());
    return setInstanceEntity;
  }

  /**
   * Maps a SetInstance entity to a SetInstanceDto.
   *
   * @param setInstance the SetInstance entity to be mapped
   * @return a SetInstanceDto populated with data from the entity
   */
  public static SetInstanceDto mapToDto(SetInstance setInstance) {
    if (setInstance == null) {
      return null;
    }
    SetInstanceDto dto = new SetInstanceDto();
    dto.setSetTagId(setInstance.getTagId());
    dto.setSetType(setInstance.getSetType() != null ? setInstance.getSetType().name() : null);
    dto.setSetBaseId(setInstance.getSetBase() != null ? setInstance.getSetBase().getId() : null);
    dto.setLocationId(setInstance.getLocation() != null ? setInstance.getLocation().getId() : null);
    dto.setSetStatus(setInstance.getSetStatus() != null ? setInstance.getSetStatus().name() : null);
    dto.setActive(setInstance.isActive());
    return dto;
  }
}
