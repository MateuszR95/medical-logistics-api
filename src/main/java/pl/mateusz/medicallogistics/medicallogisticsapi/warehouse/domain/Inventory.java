package pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.domain;

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
import pl.mateusz.medicallogistics.medicallogisticsapi.lot.domain.Lot;

/**
 * Entity representing the inventory of items at specific locations
 * in the medical logistics system.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "inventory",
    uniqueConstraints = @UniqueConstraint(
    name = "uq_inventory_location_item_lot",
    columnNames = {"location_id", "item_id", "lot_id"}
    ),
    indexes = {
      @Index(name = "ix_inventory_location", columnList = "location_id"),
      @Index(name = "ix_inventory_item", columnList = "item_id"),
      @Index(name = "ix_inventory_lot", columnList = "lot_id")
    }
)
public class Inventory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "location_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_stock_location"))
  private Location location;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "item_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_stock_item"))
  private Item item;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lot_id",
      foreignKey = @ForeignKey(name = "fk_stock_lot"),
      nullable = false)
  private Lot lot;

  @Column(nullable = false)
  private long qty;
}
