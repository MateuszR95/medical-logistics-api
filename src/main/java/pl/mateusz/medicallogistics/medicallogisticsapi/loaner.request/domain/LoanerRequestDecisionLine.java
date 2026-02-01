package pl.mateusz.medicallogistics.medicallogisticsapi.loaner.request.domain;

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
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetBase;

/**
 * Entity representing a line item in a loaner request decision,
 * linking a set base to the approved quantity.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "loaner_request_decision_line",
    uniqueConstraints = @UniqueConstraint(
    name = "uq_lrd_line_decision_setbase",
    columnNames = {"loaner_request_decision_id", "set_base_id"}
    ),
    indexes = {
      @Index(name = "ix_lrd_line_decision", columnList = "loaner_request_decision_id"),
      @Index(name = "ix_lrd_line_setbase", columnList = "set_base_id")}
)
public class LoanerRequestDecisionLine {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "loaner_request_decision_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_lrd_line_decision"))
  private LoanerRequestDecision decision;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "set_base_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_lrd_line_set_base"))
  private SetBase setBase;

  @Column(name = "approved_qty", nullable = false)
  private long approvedQty;
}
