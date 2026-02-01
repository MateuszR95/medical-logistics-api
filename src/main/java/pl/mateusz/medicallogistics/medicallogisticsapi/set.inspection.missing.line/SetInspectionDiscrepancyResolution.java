package pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.missing.line;

/**
 * Enum representing the resolution status of a set inspection discrepancy.
 */
public enum SetInspectionDiscrepancyResolution {

  PENDING,
  REPLENISH_REQUESTED,
  QUARANTINE,
  RETURN_TO_STOCK,

}
