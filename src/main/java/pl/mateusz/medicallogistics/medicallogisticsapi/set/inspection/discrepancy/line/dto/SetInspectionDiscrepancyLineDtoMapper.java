package pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.dto;

import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.domain.SetInspectionDiscrepancyLine;

/**
 * Mapper class for converting between SetInspectionDiscrepancyLine
 * domain objects and SetInspectionDiscrepancyLineDto data transfer objects.
 * This class provides a method to map a SetInspectionDiscrepancyLine
 * to a SetInspectionDiscrepancyLineDto, which is used for transferring
 * data between different layers of the application.
 */
public class SetInspectionDiscrepancyLineDtoMapper {

  /**
   * Maps a SetInspectionDiscrepancyLine domain object to a SetInspectionDiscrepancyLineDto
   * data transfer object.
   *
   * @param discrepancyLine the SetInspectionDiscrepancyLine domain object to be mapped
   * @return a SetInspectionDiscrepancyLineDto containing the mapped data from the domain object
   */
  public static SetInspectionDiscrepancyLineDto mapToDto(
      SetInspectionDiscrepancyLine discrepancyLine) {
    return SetInspectionDiscrepancyLineDto.builder()
        .itemRefNumber(discrepancyLine.getItem().getRefNumber())
        .lot(discrepancyLine.getLot().getLotNumber())
        .quantity(discrepancyLine.getQty())
        .discrepancyType(discrepancyLine.getDiscrepancyType().name())
        .build();
  }
}
