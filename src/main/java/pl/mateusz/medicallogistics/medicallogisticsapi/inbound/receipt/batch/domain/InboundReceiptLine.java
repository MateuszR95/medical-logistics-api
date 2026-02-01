package pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.batch.domain;

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
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.batch.InboundReceiptLineStatus;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.batch.InboundReceiptLineType;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.domain.Item;
import pl.mateusz.medicallogistics.medicallogisticsapi.lot.domain.Lot;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetInstance;

/**
 * Entity representing a line item in an inbound receipt batch.*/

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "inbound_receipt_line",
    indexes = {
      @Index(name = "ix_irl_batch", columnList = "batch_id"),
      @Index(name = "ix_irl_status", columnList = "status"),
      @Index(name = "ix_irl_item_ref", columnList = "item_ref_number"),
      @Index(name = "ix_irl_lot_number", columnList = "lot_number")}
)
public class InboundReceiptLine {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "batch_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_irl_batch"))
  private InboundReceiptBatch batch;

  @Column(name = "item_ref_number", nullable = false, length = 100)
  private String itemRefNumber;

  @Column(name = "lot_number", length = 80)
  private String lotNumber;

  @Column(name = "expiration_date")
  private LocalDate expirationDate;

  @Column(name = "qty", nullable = false)
  private long qty;

  // resolved references after mapping
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "resolved_item_id",
      foreignKey = @ForeignKey(name = "fk_irl_resolved_item"))
  private Item resolvedItem;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "resolved_lot_id",
      foreignKey = @ForeignKey(name = "fk_irl_resolved_lot"))
  private Lot resolvedLot;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 10)
  private InboundReceiptLineStatus status = InboundReceiptLineStatus.OK;

  @Enumerated(EnumType.STRING)
  @Column(name = "line_type", nullable = false, length = 10)
  private InboundReceiptLineType lineType = InboundReceiptLineType.ITEM;

  @Column(name = "set_tag_id", length = 20)
  private String setTagId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "resolved_set_instance_id",
      foreignKey = @ForeignKey(name = "fk_irl_resolved_set_instance"))
  private SetInstance resolvedSetInstance;

  @Column(name = "error_message", length = 500)
  private String errorMessage;


}
