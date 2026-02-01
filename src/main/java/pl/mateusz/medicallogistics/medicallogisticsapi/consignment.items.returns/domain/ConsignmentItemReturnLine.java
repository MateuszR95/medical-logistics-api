package pl.mateusz.medicallogistics.medicallogisticsapi.consignment.items.returns.domain;

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
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.medicallogistics.medicallogisticsapi.consignment.items.returns.ConsignmentItemReturnLineCheckStatus;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.domain.Item;
import pl.mateusz.medicallogistics.medicallogisticsapi.lot.domain.Lot;
import pl.mateusz.medicallogistics.medicallogisticsapi.user.domain.User;

/**
 * Entity representing a line item in a consignment item return.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "consignment_item_return_line",
    indexes = {
      @Index(name = "ix_cirl_return", columnList = "consignment_item_return_id"),
      @Index(name = "ix_cirl_item", columnList = "item_id"),
      @Index(name = "ix_cirl_lot", columnList = "lot_id"),
      @Index(name = "ix_cirl_check_status", columnList = "check_status")}
)
public class ConsignmentItemReturnLine {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "consignment_item_return_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_cirl_return")
  )
  private ConsignmentItemReturn consignmentReturn;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "item_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_cirl_item"))
  private Item item;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "lot_id",
      foreignKey = @ForeignKey(name = "fk_cirl_lot"))
  private Lot lot;

  @Column(name = "qty", nullable = false)
  private long qty;

  @Enumerated(EnumType.STRING)
  @Column(name = "check_status", nullable = false, length = 20)
  private ConsignmentItemReturnLineCheckStatus checkStatus;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "checked_by_id",
      foreignKey = @ForeignKey(name = "fk_cirl_checked_by"))
  private User checkedBy;

  @Column(name = "checked_at")
  private LocalDateTime checkedAt;

  @Column(name = "comment", length = 500)
  private String comment;
}
