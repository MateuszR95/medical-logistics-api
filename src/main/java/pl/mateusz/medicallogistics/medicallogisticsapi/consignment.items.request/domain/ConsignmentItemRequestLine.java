package pl.mateusz.medicallogistics.medicallogisticsapi.consignment.items.request.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.domain.Item;

/**
 * Entity representing a line item in a consignment item request.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "consignment_item_request_line",
    uniqueConstraints = {
      @UniqueConstraint(
        name = "uq_cirl_request_item",
        columnNames = {"consignment_item_request_id", "item_id"}) },
    indexes = {
      @Index(name = "ix_cirl_request", columnList = "consignment_item_request_id"),
      @Index(name = "ix_cirreql_item", columnList = "item_id")}
)
public class ConsignmentItemRequestLine {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "consignment_item_request_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_cireql_request"))
  private ConsignmentItemRequest request;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "item_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_cirl_item"))
  private Item item;

  @Column(name = "requested_qty", nullable = false)
  private long requestedQty;

  @Column(name = "allocated_qty", nullable = false)
  private long allocatedQty;

  @Column(name = "backordered_qty", nullable = false)
  private long backorderedQty;

  @Column(name = "shipped_qty", nullable = false)
  private long shippedQty;

  @Column(name = "comment", length = 500)
  private String comment;
}
