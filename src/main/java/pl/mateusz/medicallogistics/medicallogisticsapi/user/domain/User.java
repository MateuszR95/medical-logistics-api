package pl.mateusz.medicallogistics.medicallogisticsapi.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.medicallogistics.medicallogisticsapi.customer.domain.Customer;
import pl.mateusz.medicallogistics.medicallogisticsapi.territory.domain.Territory;
import pl.mateusz.medicallogistics.medicallogisticsapi.user.UserRole;


/**
 * Entity representing a user in the medical logistics system.
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(
    name = "users",
    uniqueConstraints = @UniqueConstraint(
    name = "uq_user_email", columnNames = "email"
    )
)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 120)
  private String email;

  @Column(name = "first_name", nullable = false, length = 80)
  private String firstName;

  @Column(name = "last_name", nullable = false, length = 80)
  private String lastName;

  @Column(name = "position_name", length = 120)
  private String positionName;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private UserRole role;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "territory_id",
      foreignKey = @ForeignKey(name = "fk_user_territory")
  )
  private Territory territory;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "reports_to_id",
      foreignKey = @ForeignKey(name = "fk_reports_to")
  )
  private User reportsTo;

  @OneToMany(mappedBy = "salesRep")
  private Set<Customer> customers = new HashSet<>();

  @Column(nullable = false)
  private boolean active = true;

  @Column(name = "password_hash", nullable = false, length = 120)
  private String passwordHash;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "password_changed_at")
  private LocalDateTime passwordChangedAt;



}

