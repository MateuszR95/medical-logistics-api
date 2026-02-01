package pl.mateusz.medicallogistics.medicallogisticsapi.purchase.order;

/**
 * Enum representing the state of a purchase order line.
 */
public enum PurchaseOrderLineState {

  ENTERED,
  BACKORDER,
  ALLOCATED,
  SHIPPED
}
