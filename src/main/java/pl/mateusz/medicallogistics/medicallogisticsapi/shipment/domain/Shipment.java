package pl.mateusz.medicallogistics.medicallogisticsapi.shipment.domain;

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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.medicallogistics.medicallogisticsapi.address.domain.Address;
import pl.mateusz.medicallogistics.medicallogisticsapi.shipment.ShipmentRefType;
import pl.mateusz.medicallogistics.medicallogisticsapi.shipment.ShipmentStatus;
import pl.mateusz.medicallogistics.medicallogisticsapi.user.domain.User;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.domain.Warehouse;

/**
 * Entity representing a shipment in the medical logistics system,
 * including details about shipment number, status, reference type,
 * shipping and delivery information, and associated users.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "shipment",
    uniqueConstraints = {
      @UniqueConstraint(name = "uq_shipment_number", columnNames = {"shipment_number"})},
    indexes = {
      @Index(name = "ix_sh_status", columnList = "status"),
      @Index(name = "ix_sh_ref", columnList = "ref_type,ref_id"),
      @Index(name = "ix_sh_ship_from_wh", columnList = "ship_from_warehouse_id"),
      @Index(name = "ix_sh_planned_ship_date", columnList = "planned_ship_date"),
      @Index(name = "ix_sh_shipped_at", columnList = "shipped_at"),
      @Index(name = "ix_sh_carrier", columnList = "carrier")}
)
public class Shipment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "shipment_number", nullable = false, length = 40)
  private String shipmentNumber;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private ShipmentStatus status = ShipmentStatus.CREATED;

  @Enumerated(EnumType.STRING)
  @Column(name = "ref_type", nullable = false, length = 40)
  private ShipmentRefType refType = ShipmentRefType.NONE;

  @Column(name = "ref_id")
  private Long refId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "ship_from_warehouse_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_sh_ship_from_wh")
  )
  private Warehouse shipFromWarehouse;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ship_to_address_id",
      foreignKey = @ForeignKey(name = "fk_sh_ship_to_address")
  )
  private Address shipToAddress;

  @Column(name = "planned_ship_date")
  private LocalDate plannedShipDate;

  @Column(name = "shipped_at")
  private LocalDateTime shippedAt;

  @Column(name = "delivered_at")
  private LocalDateTime deliveredAt;

  @Column(name = "carrier", length = 64)
  private String carrier;

  @Column(name = "tracking_number", length = 128)
  private String trackingNumber;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "created_by_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_sh_created_by"))
  private User createdBy;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "voided_by_id",
      foreignKey = @ForeignKey(name = "fk_sh_voided_by"))
  private User voidedBy;

  @Column(name = "voided_at")
  private LocalDateTime voidedAt;

  @Column(name = "comment", length = 800)
  private String comment;

  @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ShipmentLine> lines = new ArrayList<>();


}
