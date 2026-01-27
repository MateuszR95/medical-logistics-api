package pl.mateusz.medicallogistics.medicallogisticsapi.set.domain;

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
 * Entity representing the materials associated with a specific set instance
 * in the medical logistics system.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "set_instance_material",
    uniqueConstraints = @UniqueConstraint(
    name = "uq_setinst_material",
    columnNames = {"set_instance_id", "item_id", "lot_id"}
    ),
    indexes = {
      @Index(name = "ix_setinst_material_setinst", columnList = "set_instance_id"),
      @Index(name = "ix_setinst_material_item", columnList = "item_id"),
      @Index(name = "ix_setinst_material_lot", columnList = "lot_id")
    }
)
public class SetInstanceMaterial {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "set_instance_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_setinstmaterial_setinst"))
  private SetInstance setInstance;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "item_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_setinstmaterial_item"))
  private Item item;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lot_id",
      foreignKey = @ForeignKey(name = "fk_setinstmaterial_lot"))
  private Lot lot;

  @Column(name = "present_qty", nullable = false)
  private long presentQty;

  @Column(name = "missing_qty", nullable = false)
  private long missingQty = 0;

}

