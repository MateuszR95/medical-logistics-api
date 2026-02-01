package pl.mateusz.medicallogistics.medicallogisticsapi.status.history;

/**
 * Enumeration representing the types of entities that can be audited
 * in the medical logistics system.
 */
public enum AuditedEntityType {

  SHIPMENT,
  LOANER_REQUEST,
  LOANER_REQUEST_DECISION,
  PURCHASE_ORDER,
  PURCHASE_ORDER_LINE,
  CONSIGNMENT_ITEM_REQUEST,
  CONSIGNMENT_ITEM_RETURN,
  SET_INSTANCE,
  SET_RETURN,
  SET_INSPECTION,
  INBOUND_RECEIPT_BATCH
}
