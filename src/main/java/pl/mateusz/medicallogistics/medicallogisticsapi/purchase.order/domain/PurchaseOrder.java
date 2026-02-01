package pl.mateusz.medicallogistics.medicallogisticsapi.purchase.order.domain;


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
import pl.mateusz.medicallogistics.medicallogisticsapi.customer.domain.Customer;
import pl.mateusz.medicallogistics.medicallogisticsapi.loaner.request.domain.LoanerRequest;
import pl.mateusz.medicallogistics.medicallogisticsapi.purchase.order.PurchaseOrderType;
import pl.mateusz.medicallogistics.medicallogisticsapi.user.domain.User;

/**
 * Entity representing a purchase order in the medical logistics system.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "purchase_order",
    uniqueConstraints = {
      @UniqueConstraint(name = "uq_po_number", columnNames = {"po_number"})},
    indexes = {
      @Index(name = "ix_po_customer", columnList = "customer_id"),
      @Index(name = "ix_po_type", columnList = "type"),
      @Index(name = "ix_po_procedure_date", columnList = "procedure_date")})

public class PurchaseOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "po_number", nullable = false, updatable = false, length = 64)
  private String poNumber;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "customer_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_po_customer")
  )
  private Customer customer;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false, length = 32)
  private PurchaseOrderType type;

  @Column(name = "procedure_date", nullable = false)
  private LocalDate procedureDate;

  @Column(name = "customer_po_number", length = 128)
  private String customerPoNumber;

  @Column(name = "patient_data", length = 128)
  private String patientData;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ship_to_address_id",
      foreignKey = @ForeignKey(name = "fk_po_ship_to_address"))
  private Address shipToAddress;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "source_loaner_request_id",
      foreignKey = @ForeignKey(name = "fk_po_source_loaner_request")
  )
  private LoanerRequest sourceLoanerRequest;

  @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PurchaseOrderLine> lines = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "entered_by_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_po_entered_by"))
  private User enteredBy;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "modified_by_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_po_modified_by"))
  private User modifiedBy;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

}
