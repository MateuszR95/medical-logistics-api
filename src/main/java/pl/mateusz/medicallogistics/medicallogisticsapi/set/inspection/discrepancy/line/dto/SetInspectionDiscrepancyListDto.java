package pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for a list of set inspection discrepancy lines.
 * This DTO is used to encapsulate multiple discrepancy lines when creating or updating
 * discrepancies for a set inspection.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetInspectionDiscrepancyListDto {

  @NotNull
  @Valid
  private List<SetInspectionDiscrepancyLineDto> lines = new ArrayList<>();
}
