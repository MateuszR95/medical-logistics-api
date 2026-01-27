package pl.mateusz.medicallogistics.medicallogisticsapi.set.domain;

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
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.medicallogistics.medicallogisticsapi.customer.domain.Customer;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.SetStatus;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.SetType;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.domain.Location;

/**
 * Entity representing an instance of a set in the medical logistics system.
 * This entity tracks the specific details of a set instance, including its
 * unique tag, type, associated set base, location, status, and customer.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "set_instance",
    uniqueConstraints = {
      @UniqueConstraint(name = "uq_set_instance_tag", columnNames = "tag_id")
    },
    indexes = {
      @Index(name = "ix_setinst_setbase", columnList = "set_base_id"),
      @Index(name = "ix_setinst_location", columnList = "location_id"),
      @Index(name = "ix_setinst_customer", columnList = "customer_id"),
      @Index(name = "ix_setinst_status", columnList = "set_status"),
      @Index(name = "ix_setinst_active", columnList = "active")
    }
)
public class SetInstance {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "tag_id", nullable = false, length = 20)
  private String tagId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private SetType setType;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "set_base_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_set_instance_set_base"))
  private SetBase setBase;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "location_id",
      foreignKey = @ForeignKey(name = "fk_set_instance_location"))
  private Location location;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private SetStatus setStatus;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id",
      foreignKey = @ForeignKey(name = "fk_set_instance_customer"))
  private Customer customer;

  @Column(name = "shipped_date")
  private LocalDate shippedDate;

  @Column(name = "surgery_date")
  private LocalDate surgeryDate;

  @Column(name = "expected_return_date")
  private LocalDate expectedReturnDate;

  @Column(nullable = false)
  private boolean active = true;


}
