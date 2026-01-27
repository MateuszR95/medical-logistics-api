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


/**
 * Entity representing the relationship between a set base and its materials
 * in the medical logistics system.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "set_base_material",
    uniqueConstraints = @UniqueConstraint(
    name = "uq_setbase_material",
    columnNames = {"set_base_id", "item_id"}
    ),
    indexes = {
      @Index(name = "ix_setbasematerial_setbase", columnList = "set_base_id"),
      @Index(name = "ix_setbasematerial_item", columnList = "item_id")
    }
)
public class SetBaseMaterial {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "set_base_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_setbasematerial_setbase"))
  private SetBase setBase;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "item_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_setbasematerial_item"))
  private Item item;

  @Column(name = "required_qty", nullable = false)
  private long requiredQty;
}
