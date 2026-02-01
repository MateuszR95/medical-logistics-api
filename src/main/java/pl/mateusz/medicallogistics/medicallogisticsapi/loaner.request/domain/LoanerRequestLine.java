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
 * Entity representing a line item in a loaner request,
 * linking a set base to the requested quantity.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "loaner_request_line",
    uniqueConstraints = @UniqueConstraint(
    name = "uq_lr_line_request_setbase",
    columnNames = {"loaner_request_id", "set_base_id"}
    ),
    indexes = {
      @Index(name = "ix_lr_line_request", columnList = "loaner_request_id"),
      @Index(name = "ix_lr_line_setbase", columnList = "set_base_id")}
)
public class LoanerRequestLine {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "loaner_request_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_lr_line_request"))
  private LoanerRequest loanerRequest;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "set_base_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_lr_line_set_base"))
  private SetBase setBase;

  @Column(name = "requested_qty", nullable = false)
  private long requestedQty;

}
