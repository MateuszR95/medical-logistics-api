package pl.mateusz.medicallogistics.medicallogisticsapi.status.history.domain;

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
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.medicallogistics.medicallogisticsapi.status.history.AuditedEntityType;
import pl.mateusz.medicallogistics.medicallogisticsapi.user.domain.User;

/**
 * Entity representing a historical record of status changes for various entities
 * within the medical logistics system.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "status_history",
    indexes = {
      @Index(name = "ix_sth_entity", columnList = "entity_type,entity_id"),
      @Index(name = "ix_sth_changed_at", columnList = "changed_at"),
      @Index(name = "ix_sth_changed_by", columnList = "changed_by_id")}
)
public class StatusHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "entity_type", nullable = false, length = 40)
  private AuditedEntityType entityType;

  @Column(name = "entity_id", nullable = false)
  private Long entityId;

  @Column(name = "field_name", nullable = false, length = 60)
  private String fieldName = "status";

  @Column(name = "from_value", length = 60)
  private String fromValue;

  @Column(name = "to_value", nullable = false, length = 60)
  private String toValue;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "changed_by_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_sth_changed_by"))
  private User changedBy;

  @Column(name = "changed_at", nullable = false)
  private LocalDateTime changedAt;

  @Column(name = "comment", length = 800)
  private String comment;


}
