package pl.mateusz.medicallogistics.medicallogisticsapi.shipment;

/**
 * Enum representing the reference type for a shipment.
 */
public enum ShipmentRefType {

  NONE,
  CONSIGNMENT_ITEM_REQUEST,
  LOANER_REQUEST,
  PURCHASE_ORDER,
  PURCHASE_ORDER_LINE,
  SET_INSTANCE,
  SET_RETURN
}
