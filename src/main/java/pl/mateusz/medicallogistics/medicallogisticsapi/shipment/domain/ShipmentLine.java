package pl.mateusz.medicallogistics.medicallogisticsapi.shipment.domain;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.domain.Item;
import pl.mateusz.medicallogistics.medicallogisticsapi.lot.domain.Lot;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetInstance;
import pl.mateusz.medicallogistics.medicallogisticsapi.shipment.ShipmentLineType;
import pl.mateusz.medicallogistics.medicallogisticsapi.shipment.ShipmentRefType;

/**
 * Entity representing a line item within a shipment,
 * including details about the item, lot, quantity, and references.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "shipment_line",
    indexes = {
      @Index(name = "ix_shl_shipment", columnList = "shipment_id"),
      @Index(name = "ix_shl_line_type", columnList = "line_type"),
      @Index(name = "ix_shl_item", columnList = "item_id"),
      @Index(name = "ix_shl_lot", columnList = "lot_id"),
      @Index(name = "ix_shl_setinst", columnList = "set_instance_id"),
      @Index(name = "ix_shl_ref", columnList = "source_ref_type,source_ref_id")}
)
public class ShipmentLine {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "shipment_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_shl_shipment"))
  private Shipment shipment;

  @Enumerated(EnumType.STRING)
  @Column(name = "line_type", nullable = false, length = 10)
  private ShipmentLineType lineType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id",
      foreignKey = @ForeignKey(name = "fk_shl_item"))
  private Item item;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lot_id",
      foreignKey = @ForeignKey(name = "fk_shl_lot"))
  private Lot lot;

  @Column(name = "qty", nullable = false)
  private long qty;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "set_instance_id",
      foreignKey = @ForeignKey(name = "fk_shl_set_instance"))
  private SetInstance setInstance;

  @Enumerated(EnumType.STRING)
  @Column(name = "source_ref_type", nullable = false, length = 40)
  private ShipmentRefType sourceRefType = ShipmentRefType.NONE;

  @Column(name = "source_ref_id")
  private Long sourceRefId;

  @Column(name = "comment", length = 500)
  private String comment;


}
