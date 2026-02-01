package pl.mateusz.medicallogistics.medicallogisticsapi.loaner.request.domain;

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
import jakarta.persistence.OneToOne;
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
import pl.mateusz.medicallogistics.medicallogisticsapi.loaner.request.LoanerRequestStatus;
import pl.mateusz.medicallogistics.medicallogisticsapi.user.domain.User;

/**
 * Entity representing a loaner request in the medical logistics system.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "loaner_request",
    uniqueConstraints = @UniqueConstraint(name = "uq_loaner_request_number",
      columnNames = "request_number"),
    indexes = {
      @Index(name = "ix_lr_customer", columnList = "customer_id"),
      @Index(name = "ix_lr_status", columnList = "status"),
      @Index(name = "ix_lr_planned_shipping_date", columnList = "planned_shipping_date"),
      @Index(name = "ix_lr_shipped_date", columnList = "shipped_date")}
)
public class LoanerRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "request_number", nullable = false, length = 30)
  private String requestNumber;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "customer_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_lr_customer"))
  private Customer customer;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "requested_by_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_lr_requested_by"))
  private User requestedBy;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "modified_by_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_lr_modified_by"))
  private User modifiedBy;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ship_to_address_id",
      foreignKey = @ForeignKey(name = "fk_lr_ship_to_address"))
  private Address shipToAddress;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Column(name = "voided_at")
  private LocalDateTime voidedAt;

  @Column(name = "planned_shipping_date")
  private LocalDate plannedShippingDate;

  @Column(name = "surgery_date")
  private LocalDate surgeryDate;

  @Column(name = "shipped_date")
  private LocalDate shippedDate;

  @Column(name = "expected_return_date")
  private LocalDate expectedReturnDate;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private LoanerRequestStatus status;

  @Column(name = "notes", length = 1000)
  private String notes;

  @OneToMany(mappedBy = "loanerRequest", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<LoanerRequestLine> lines = new ArrayList<>();

  @OneToOne(mappedBy = "loanerRequest", fetch = FetchType.LAZY)
  private LoanerRequestDecision decision;
}
