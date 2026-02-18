package pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.domain;

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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.medicallogistics.medicallogisticsapi.customer.domain.Customer;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.SetInspectionStatus;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.domain.SetInspectionDiscrepancyLine;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.returns.domain.SetReceipt;
import pl.mateusz.medicallogistics.medicallogisticsapi.user.domain.User;


/**
 * Entity representing an inspection of a returned set in the medical logistics system.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "set_inspection",
    uniqueConstraints = {
      @UniqueConstraint(name = "uq_si_return", columnNames = {"set_receipt_id"})},
    indexes = {
      @Index(name = "ix_si_inspected_at", columnList = "inspected_at"),
      @Index(name = "ix_si_inspected_by", columnList = "inspected_by_id"),
      @Index(name = "ix_si_received_from_customer", columnList = "received_from_customer_id"),
      @Index(name = "ix_si_status", columnList = "status"),
      @Index(name = "ix_si_closed_at", columnList = "closed_at"),
      @Index(name = "ix_si_closed_by", columnList = "closed_by_id")}
)
public class SetInspection {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "set_receipt_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_si_set_receipt")
  )
  private SetReceipt setReceipt;

  @Column(name = "inspected_at", nullable = false)
  private LocalDateTime inspectedAt;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "inspected_by_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_si_inspected_by")
  )
  private User inspectedBy;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "received_from_customer_id",
      foreignKey = @ForeignKey(name = "fk_si_received_from_customer")
  )
  private Customer receivedFromCustomer;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private SetInspectionStatus status = SetInspectionStatus.OPEN;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "closed_by_id",
      foreignKey = @ForeignKey(name = "fk_si_closed_by")
  )
  private User closedBy;

  @Column(name = "closed_at")
  private LocalDateTime closedAt;

  @Column(name = "comment", length = 800)
  private String comment;

  @OneToMany(mappedBy = "inspection", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<SetInspectionDiscrepancyLine> discrepancyLines = new ArrayList<>();

}
