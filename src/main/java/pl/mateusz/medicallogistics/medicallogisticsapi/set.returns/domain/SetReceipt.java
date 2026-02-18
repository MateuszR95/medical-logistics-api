package pl.mateusz.medicallogistics.medicallogisticsapi.set.returns.domain;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetInstance;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.domain.SetInspection;
import pl.mateusz.medicallogistics.medicallogisticsapi.user.domain.User;

/**
 * Entity representing the return of a medical set,
 * including details about the return number, associated set instance,
 * reception details, and any comments.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "set_receipt",
    uniqueConstraints = {
      @UniqueConstraint(name = "uq_set_receipt_number", columnNames = {"receipt_number"})
    },
    indexes = {
      @Index(name = "ix_sr_set_instance", columnList = "set_instance_id"),
      @Index(name = "ix_sr_received_at", columnList = "received_at")}
)
public class SetReceipt {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "receipt_number", nullable = false, length = 30)
  private String receiptNumber;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "set_instance_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_sr_set_instance")
  )
  private SetInstance setInstance;

  @Column(name = "received_at", nullable = false)
  private LocalDateTime receivedAt;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "received_by_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_sr_received_by")
  )
  private User receivedBy;

  @Column(name = "comment", length = 1000)
  private String comment;

  @OneToOne(mappedBy = "setReceipt", fetch = FetchType.LAZY)
  private SetInspection inspection;
}
