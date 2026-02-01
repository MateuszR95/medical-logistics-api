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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.medicallogistics.medicallogisticsapi.loaner.request.LoanerDecisionStatus;
import pl.mateusz.medicallogistics.medicallogisticsapi.user.domain.User;

/**
 * Entity representing a decision made on a loaner request in the medical logistics system.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "loaner_request_decision",
    uniqueConstraints = @UniqueConstraint(
    name = "uq_lrd_request",
    columnNames = "loaner_request_id"),
    indexes = {
      @Index(name = "ix_lrd_status", columnList = "status"),
      @Index(name = "ix_lrd_decided_by", columnList = "decided_by_id")}
)
public class LoanerRequestDecision {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "loaner_request_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_lrd_request"))
  private LoanerRequest loanerRequest;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private LoanerDecisionStatus status;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "decided_by_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_lrd_decided_by"))
  private User decidedBy;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "modified_by_id",
      foreignKey = @ForeignKey(name = "fk_lrd_modified_by"))
  private User modifiedBy;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "voided_at")
  private LocalDateTime voidedAt;

  @Column(name = "comment", length = 800)
  private String comment;

  @OneToMany(
      mappedBy = "decision",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private List<LoanerRequestDecisionLine> lines = new ArrayList<>();
}
