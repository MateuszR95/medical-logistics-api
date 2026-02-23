package pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.dto;

import java.util.List;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.dto.SetInspectionDiscrepancyLineDto;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.dto.SetInspectionDiscrepancyLineDtoMapper;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.domain.SetInspection;


/**
 * Mapper class for converting between SetInspection domain objects
 * and SetInspectionDto data transfer objects.
 * This class provides a method to map a SetInspection to a SetInspectionDto, which
 * is used for transferring data between different layers of the application.
 */
public class SetInspectionDtoMapper {

  /**
   * Maps a SetInspection domain object to a SetInspectionDto data transfer object.
   *
   * @param setInspection the SetInspection domain object to be mapped
   * @return a SetInspectionDto containing the mapped data from the domain object
   */
  public static SetInspectionDto mapToDto(SetInspection setInspection) {
    List<SetInspectionDiscrepancyLineDto> discrepancyLinesDto = setInspection
        .getDiscrepancyLines()
        .stream()
        .map(SetInspectionDiscrepancyLineDtoMapper::mapToDto)
        .toList();
    return SetInspectionDto.builder()
        .inspectedByEmail(setInspection.getInspectedBy().getEmail())
        .inspectedAt(setInspection.getInspectedAt().toString())
        .discrepancies(discrepancyLinesDto)
        .inspectionNumber(setInspection.getSetInspectionNumber())
        .build();
  }
}
