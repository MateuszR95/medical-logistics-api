package pl.mateusz.medicallogistics.medicallogisticsapi.set.receipt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for representing the details of a set receipt.
 * This DTO is used to transfer information about a set receipt, including the
 * associated set tag ID, receipt number, reception date, and receiver's email.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetReceiptDto {

  private String setTagId;
  private String receiptNumber;
  private String receivedAt;
  private String receiverEmail;

}
