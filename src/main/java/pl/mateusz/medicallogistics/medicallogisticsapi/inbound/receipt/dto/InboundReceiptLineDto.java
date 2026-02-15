package pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.medicallogistics.medicallogisticsapi.validation.CorrectInboundReceiptLineType;
import pl.mateusz.medicallogistics.medicallogisticsapi.validation.ItemRefNumberExists;
import pl.mateusz.medicallogistics.medicallogisticsapi.validation.SetCatalogNumberExists;
import pl.mateusz.medicallogistics.medicallogisticsapi.validation.ValidInboundReceiptLine;

/**
 * Data Transfer Object representing a line item in an inbound receipt.
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ValidInboundReceiptLine
public class InboundReceiptLineDto {

  @NotBlank(message = "Line type cannot be blank")
  @CorrectInboundReceiptLineType
  private String lineType;
  @NotBlank(message = "Item reference number cannot be blank")
  @ItemRefNumberExists
  private String itemRefNumber;
  @NotBlank(message = "Lot number cannot be blank")
  @Pattern(
      regexp = "^[A-Z]{3}[0-9]{3}[A-Z]{2}$",
      message = "Lot number must match pattern: ABC123AB"
  )
  private String lotNumber;
  @Pattern(
      regexp = "^$|^\\d{4}-\\d{2}-\\d{2}$",
      message = "Expiration date must be in the format YYYY-MM-DD"
  )
  private String expirationDate;
  @NotNull(message = "Quantity cannot be null")
  @Min(1)
  private long qty;
  @SetCatalogNumberExists
  private String setCatalogNumber;
  private String setTagId;
  private Long batchId;


}
