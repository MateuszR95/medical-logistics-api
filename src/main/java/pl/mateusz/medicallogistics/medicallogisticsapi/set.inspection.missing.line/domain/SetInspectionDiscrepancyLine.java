package pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.missing.line.domain;

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
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.domain.Item;
import pl.mateusz.medicallogistics.medicallogisticsapi.loaner.request.domain.LoanerRequest;
import pl.mateusz.medicallogistics.medicallogisticsapi.lot.domain.Lot;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.domain.SetInspection;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.missing.line.SetInspectionDiscrepancyResolution;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.missing.line.SetInspectionDiscrepancyType;

/**
 * Entity representing a discrepancy line item found during a set inspection,
 * detailing the item, lot, type of discrepancy, quantity, resolution, and
 * any associated loaner request.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "set_inspection_discrepancy_line",
    uniqueConstraints = {
      @UniqueConstraint(
        name = "uq_sidl_inspection_item_lot_type",
        columnNames = {"set_inspection_id", "item_id", "lot_id", "discrepancy_type"})},
    indexes = {
      @Index(name = "ix_sidl_inspection", columnList = "set_inspection_id"),
      @Index(name = "ix_sidl_item", columnList = "item_id"),
      @Index(name = "ix_sidl_lot", columnList = "lot_id"),
      @Index(name = "ix_sidl_type", columnList = "discrepancy_type"),
      @Index(name = "ix_sidl_resolution", columnList = "resolution"),
      @Index(name = "ix_sidl_loaner_request", columnList = "loaner_request_id")}
)
public class SetInspectionDiscrepancyLine {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "set_inspection_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_sidl_inspection")
  )
  private SetInspection inspection;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "item_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_sidl_item")
  )
  private Item item;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "lot_id",
      foreignKey = @ForeignKey(name = "fk_sidl_lot")
  )
  private Lot lot;

  @Enumerated(EnumType.STRING)
  @Column(name = "discrepancy_type", nullable = false, length = 20)
  private SetInspectionDiscrepancyType discrepancyType;

  @Column(name = "qty", nullable = false)
  private long qty;

  @Enumerated(EnumType.STRING)
  @Column(name = "resolution", nullable = false, length = 40)
  private SetInspectionDiscrepancyResolution resolution =
      SetInspectionDiscrepancyResolution.PENDING;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "loaner_request_id",
      foreignKey = @ForeignKey(name = "fk_sidl_loaner_request")
  )
  private LoanerRequest loanerRequest;

  @Column(name = "comment", length = 800)
  private String comment;
}

