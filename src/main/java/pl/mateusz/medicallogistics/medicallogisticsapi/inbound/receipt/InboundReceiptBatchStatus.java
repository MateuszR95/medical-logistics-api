package pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt;

/**
 * Enum representing the status of an inbound receipt batch.
 */
public enum InboundReceiptBatchStatus {

  IMPORTED,
  POSTED,
  FAILED,
  CANCELLED
}
