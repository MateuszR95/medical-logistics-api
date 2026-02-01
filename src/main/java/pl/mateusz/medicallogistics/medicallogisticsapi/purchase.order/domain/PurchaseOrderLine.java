package pl.mateusz.medicallogistics.medicallogisticsapi.purchase.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.domain.Item;
import pl.mateusz.medicallogistics.medicallogisticsapi.purchase.order.PurchaseOrderLineState;

/**
 * Entity representing a line item in a purchase order,
 * linking an item to the ordered quantities and shipment details.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "purchase_order_line",
    indexes = {
      @Index(name = "ix_po_line_item", columnList = "item_id"),
      @Index(name = "ix_po_line_state", columnList = "state"),
      @Index(name = "ix_po_line_shipped_at", columnList = "shipped_at")}
)
public class PurchaseOrderLine {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "purchase_order_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_po_line_po")
  )
  private PurchaseOrder purchaseOrder;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "item_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_po_line_item")
  )
  private Item item;

  @Column(name = "qty_total", nullable = false)
  private int qtyTotal;

  @Column(name = "qty_allocated", nullable = false)
  private int qtyAllocated = 0;

  @Column(name = "qty_shipped", nullable = false)
  private int qtyShipped = 0;

  @Column(name = "qty_backorder", nullable = false)
  private int qtyBackorder = 0;

  @Enumerated(EnumType.STRING)
  @Column(name = "state", nullable = false, length = 32)
  private PurchaseOrderLineState state = PurchaseOrderLineState.ENTERED;

  @Column(name = "carrier", length = 64)
  private String carrier;

  @Column(name = "tracking_number", length = 128)
  private String trackingNumber;

  @Column(name = "shipped_at")
  private LocalDate shippedAt;


}
