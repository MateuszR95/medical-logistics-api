package pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.batch.domain;


import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.batch.InboundReceiptBatchStatus;
import pl.mateusz.medicallogistics.medicallogisticsapi.user.domain.User;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.domain.Warehouse;

/**
 * Entity representing a batch of inbound receipts in the medical logistics system.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "inbound_receipt_batch",
    uniqueConstraints = {
      @UniqueConstraint(name = "uq_irb_batch_number", columnNames = {"batch_number"})},
    indexes = {
      @Index(name = "ix_irb_status", columnList = "status"),
      @Index(name = "ix_irb_received_to_wh", columnList = "received_to_warehouse_id"),
      @Index(name = "ix_irb_created_at", columnList = "created_at")}
)
public class InboundReceiptBatch {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "batch_number", nullable = false, length = 40)
  private String batchNumber;

  @Column(name = "file_name", nullable = false, length = 200)
  private String fileName;

  @Column(name = "source_warehouse_code", length = 30)
  private String sourceWarehouseCode;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "received_to_warehouse_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_irb_received_to_wh"))
  private Warehouse receivedToWarehouse;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private InboundReceiptBatchStatus status = InboundReceiptBatchStatus.IMPORTED;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "created_by_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_irb_created_by"))
  private User createdBy;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "posted_by_id",
      foreignKey = @ForeignKey(name = "fk_irb_posted_by"))
  private User postedBy;

  @Column(name = "posted_at")
  private LocalDateTime postedAt;

  @Column(name = "comment", length = 800)
  private String comment;

  @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<InboundReceiptLine> lines = new ArrayList<>();


}
