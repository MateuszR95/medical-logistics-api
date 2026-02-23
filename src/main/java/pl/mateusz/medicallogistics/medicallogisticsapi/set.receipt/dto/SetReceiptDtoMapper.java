package pl.mateusz.medicallogistics.medicallogisticsapi.set.receipt.dto;

import pl.mateusz.medicallogistics.medicallogisticsapi.set.receipt.domain.SetReceipt;

/**
 * Mapper class for converting SetReceipt entities to SetReceiptDto objects.
 * This class provides a method to map the details of a SetReceipt entity
 * to a corresponding SetReceiptDto, which can be used for data transfer
 * in API responses or other layers of the application.
 */
public class SetReceiptDtoMapper {

  /**
   * Maps a SetReceipt entity to a SetReceiptDto.
   *
   * @param setReceipt The SetReceipt entity to be mapped.
   * @return A SetReceiptDto containing the mapped details from the SetReceipt entity.
   */
  public static SetReceiptDto mapToDto(SetReceipt setReceipt) {
    return SetReceiptDto.builder()
        .setTagId(setReceipt.getSetInstance().getTagId())
        .receiptNumber(setReceipt.getReceiptNumber())
        .receivedAt(setReceipt.getReceivedAt().toString())
        .receiverEmail(setReceipt.getReceivedBy().getEmail())
        .build();
  }
}
