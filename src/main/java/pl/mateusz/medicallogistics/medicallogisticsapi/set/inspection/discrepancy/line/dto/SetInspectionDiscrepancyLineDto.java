package pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.medicallogistics.medicallogisticsapi.validation.ValidSetInspectionDiscrepancyType;

/**
 * Data Transfer Object for creating or updating a set inspection discrepancy line.
 * This DTO captures the necessary information to identify the item, lot, type of
 * discrepancy, quantity, resolution, and any associated loaner request.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetInspectionDiscrepancyLineDto {

  @NotBlank
  private String itemRefNumber;
  @NotBlank
  private String lot;
  @NotNull
  @Min(1)
  private Long quantity;
  @NotBlank
  @ValidSetInspectionDiscrepancyType
  private String discrepancyType;

}
