package pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object representing missing items in a set inspection.
 * This DTO contains information about the item reference number, item name,
 * required quantity, present quantity, and missing quantity for a specific item
 * in the context of a set inspection.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetMissingItemDto {

  private String itemRefNumber;
  private String itemName;
  private long requiredQty;
  private long presentQty;
  private long missingQty;
}
