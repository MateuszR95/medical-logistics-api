package pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement;

/**
 * Enum representing the reference type for a stock movement.
 */
public enum StockMovementRefType {

  NONE,
  SET_INSPECTION,
  SET_RETURN,
  SHIPMENT,
  CONSIGNMENT_ITEM_REQUEST,
  PURCHASE_ORDER,
  PURCHASE_ORDER_LINE,
  INBOUND_RECEIPT_BATCH,
  INVENTORY_ALLOCATION
}
