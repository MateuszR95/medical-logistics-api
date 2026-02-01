package pl.mateusz.medicallogistics.medicallogisticsapi.consignment.items.request;


/**
 * Enum representing the type of a consignment item request.
 */
public enum ConsignmentItemRequestType {
  REPLACEMENT_FOR_DAMAGED,
  REPLACEMENT_FOR_EXPIRED,
  CONSIGNMENT_EXTENSION,
  SET_REPLENISHMENT
}
