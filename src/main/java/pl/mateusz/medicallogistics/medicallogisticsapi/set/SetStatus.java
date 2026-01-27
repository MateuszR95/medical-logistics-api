package pl.mateusz.medicallogistics.medicallogisticsapi.set;

/**
 * Enum representing the status of a set in the medical logistics system.
 */
public enum SetStatus {
  AVAILABLE,
  ALLOCATED,
  IN_CUSTOMER,
  RETURNED_PENDING_CHECK,
  QUARANTINED
}
