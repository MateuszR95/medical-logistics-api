package pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.dto.SetInspectionDiscrepancyLineDto;

/**
 * Data Transfer Object representing the details of a set inspection.
 * This DTO contains information about the inspector (email and name) and a list of discrepancies
 * found during the inspection. It is used to transfer data related to a set inspection between
 * different layers of the application, such as from the controller to the service layer.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetInspectionDto {


  private String inspectedByEmail;
  private String inspectedAt;
  private String inspectionNumber;
  private List<SetInspectionDiscrepancyLineDto> discrepancies;
}
