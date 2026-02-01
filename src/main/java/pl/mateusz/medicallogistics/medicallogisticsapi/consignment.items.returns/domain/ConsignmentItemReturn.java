package pl.mateusz.medicallogistics.medicallogisticsapi.consignment.items.returns.domain;

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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.medicallogistics.medicallogisticsapi.consignment.items.returns.ConsignmentItemReturnStatus;
import pl.mateusz.medicallogistics.medicallogisticsapi.consignment.items.returns.ConsignmentItemReturnType;
import pl.mateusz.medicallogistics.medicallogisticsapi.customer.domain.Customer;
import pl.mateusz.medicallogistics.medicallogisticsapi.user.domain.User;


/**
 * Entity representing a consignment item return in the medical logistics system.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "consignment_item_return",
    uniqueConstraints = {
      @UniqueConstraint(
        name = "uq_cir_return_number",
        columnNames = {"return_number"})},
    indexes = {
      @Index(name = "ix_cir_return_customer", columnList = "customer_id"),
      @Index(name = "ix_cir_return_status", columnList = "status"),
      @Index(name = "ix_cir_return_type", columnList = "type"),
      @Index(name = "ix_cir_return_received_date", columnList = "received_date")}
)
public class ConsignmentItemReturn {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "return_number", nullable = false, length = 30)
  private String returnNumber;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "customer_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_cir_return_customer")
  )
  private Customer customer;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false, length = 40)
  private ConsignmentItemReturnType type;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 30)
  private ConsignmentItemReturnStatus status;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "created_by_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_cir_return_created_by")
  )
  private User createdBy;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "received_by_id",
      foreignKey = @ForeignKey(name = "fk_cir_return_received_by")
  )
  private User receivedBy;

  @Column(name = "received_at")
  private LocalDateTime receivedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "checked_by_id",
      foreignKey = @ForeignKey(name = "fk_cir_checked_by"))
  private User checkedBy;

  @Column(name = "checked_at")
  private LocalDateTime checkedAt;

  @Lob
  @Column(name = "email_content")
  private String emailContent;

  @Column(name = "received_date")
  private LocalDate receivedDate;

  @Column(name = "tracking_number", length = 120)
  private String trackingNumber;

  @Column(name = "carrier", length = 50)
  private String carrier;

  @Column(name = "comment", length = 1000)
  private String comment;

  @OneToMany(
      mappedBy = "consignmentReturn",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private List<ConsignmentItemReturnLine> lines = new ArrayList<>();
}
