package pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.domain;

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
import pl.mateusz.medicallogistics.medicallogisticsapi.item.domain.Item;
import pl.mateusz.medicallogistics.medicallogisticsapi.lot.domain.Lot;
import pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.StockMovementRefType;
import pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.StockMovementType;
import pl.mateusz.medicallogistics.medicallogisticsapi.user.domain.User;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.domain.Location;

/**
 * Entity representing a stock movement within the medical logistics system,
 * including details about the movement type, locations, item, lot, quantity,
 * posting user and time, reference type and ID, and any comments.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "stock_movement",
    indexes = {
      @Index(name = "ix_sm_posted_at", columnList = "posted_at"),
      @Index(name = "ix_sm_type", columnList = "movement_type"),
      @Index(name = "ix_sm_from_location", columnList = "from_location_id"),
      @Index(name = "ix_sm_to_location", columnList = "to_location_id"),
      @Index(name = "ix_sm_item", columnList = "item_id"),
      @Index(name = "ix_sm_lot", columnList = "lot_id"),
      @Index(name = "ix_sm_ref", columnList = "ref_type,ref_id")}
)
public class StockMovement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "movement_type", nullable = false, length = 20)
  private StockMovementType movementType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "from_location_id",
      foreignKey = @ForeignKey(name = "fk_sm_from_location"))
  private Location fromLocation;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "to_location_id",
      foreignKey = @ForeignKey(name = "fk_sm_to_location"))
  private Location toLocation;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "item_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_sm_item"))
  private Item item;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lot_id",
      foreignKey = @ForeignKey(name = "fk_sm_lot"))
  private Lot lot;

  @Column(name = "qty", nullable = false)
  private long qty;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "posted_by_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_sm_posted_by"))
  private User postedBy;

  @Column(name = "posted_at", nullable = false)
  private LocalDateTime postedAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "ref_type", nullable = false, length = 40)
  private StockMovementRefType refType = StockMovementRefType.NONE;

  @Column(name = "ref_id")
  private Long refId;

  @Column(name = "comment", length = 800)
  private String comment;
}
