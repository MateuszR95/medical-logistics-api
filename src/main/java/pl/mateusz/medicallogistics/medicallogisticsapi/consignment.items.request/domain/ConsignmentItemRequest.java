package pl.mateusz.medicallogistics.medicallogisticsapi.consignment.items.request.domain;


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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.medicallogistics.medicallogisticsapi.address.domain.Address;
import pl.mateusz.medicallogistics.medicallogisticsapi.consignment.items.request.ConsignmentItemRequestStatus;
import pl.mateusz.medicallogistics.medicallogisticsapi.consignment.items.request.ConsignmentItemRequestType;
import pl.mateusz.medicallogistics.medicallogisticsapi.customer.domain.Customer;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetInstance;
import pl.mateusz.medicallogistics.medicallogisticsapi.user.domain.User;


/**
 * Entity representing a consignment item request in the medical logistics system.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "consignment_item_request",
    uniqueConstraints = {
      @UniqueConstraint(name = "uq_cir_request_number", columnNames = {"request_number"})
    },
    indexes = {
      @Index(name = "ix_cir_customer", columnList = "customer_id"),
      @Index(name = "ix_cir_status", columnList = "status"),
      @Index(name = "ix_cir_planned_shipping_date", columnList = "planned_shipping_date"),
      @Index(name = "ix_cir_shipped_at", columnList = "shipped_at"),
      @Index(name = "ix_cir_source_ref", columnList = "source_ref"),
      @Index(name = "ix_cir_set_instance", columnList = "set_instance_id")}
)
public class ConsignmentItemRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "request_number", nullable = false, length = 30)
  private String requestNumber;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "customer_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_cir_customer"))
  private Customer customer;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false, length = 40)
  private ConsignmentItemRequestType type;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 30)
  private ConsignmentItemRequestStatus status;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "created_by_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_cir_created_by"))
  private User createdBy;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Lob
  @Column(name = "email_content")
  private String emailContent;

  @Column(name = "planned_shipping_date")
  private LocalDate plannedShippingDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "picked_by_id",
      foreignKey = @ForeignKey(name = "fk_cir_picked_by"))
  private User pickedBy;

  @Column(name = "picked_at")
  private LocalDateTime pickedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "shipped_by_id",
      foreignKey = @ForeignKey(name = "fk_cir_shipped_by"))
  private User shippedBy;

  @Column(name = "shipped_at")
  private LocalDateTime shippedAt;

  @Column(name = "tracking_number", length = 120)
  private String trackingNumber;

  @Column(name = "carrier", length = 50)
  private String carrier;

  @Column(name = "comment", length = 1000)
  private String comment;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "parent_request_id",
      foreignKey = @ForeignKey(name = "fk_cir_parent_request")
  )
  private ConsignmentItemRequest parentRequest;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "set_instance_id",
      foreignKey = @ForeignKey(name = "fk_cir_set_instance")
  )
  private SetInstance setInstance;

  @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ConsignmentItemRequestLine> lines = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ship_to_address_id",
      foreignKey = @ForeignKey(name = "fk_cir_ship_to_address"))
  private Address shipToAddress;

  @Column(name = "source_ref", length = 80)
  private String sourceRef;
}
