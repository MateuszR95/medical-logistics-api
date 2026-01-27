package pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.domain;

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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.LocationZone;

/**
 * Class representing a specific location within a warehouse.
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "location",
    uniqueConstraints = @UniqueConstraint(
    name = "uq_location_warehouse_code",
    columnNames = {"warehouse_id", "code"}
    )
)
public class Location {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "warehouse_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_location_warehouse"))
  private Warehouse warehouse;

  @Column(nullable = false, length = 40)
  private String code;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private LocationZone zone;

  @Column(nullable = false)
  private boolean active = true;
}
