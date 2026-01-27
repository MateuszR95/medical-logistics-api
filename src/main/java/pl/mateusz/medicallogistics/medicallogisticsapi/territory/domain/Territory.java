package pl.mateusz.medicallogistics.medicallogisticsapi.territory.domain;

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
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.medicallogistics.medicallogisticsapi.country.domain.Country;
import pl.mateusz.medicallogistics.medicallogisticsapi.territory.TerritoryType;


/**
 * Entity representing a territory in the medical logistics system.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "territory",
    uniqueConstraints = @UniqueConstraint(name = "uq_territory_code",
      columnNames = "code")
)
public class Territory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 40)
  private String code; // EU, NORTH_EU, PL, PL-NORTH, PL-SOUTH

  @Column(nullable = false, length = 120)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "territory_type", nullable = false, length = 20)
  private TerritoryType type;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id",
      foreignKey = @ForeignKey(name = "fk_territory_parent"))
  private Territory parent;

  @OneToMany(mappedBy = "parent")
  private Set<Territory> children = new HashSet<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "country_id",
      foreignKey = @ForeignKey(name = "fk_territory_country"))
  private Country country;

  @Column(nullable = false)
  private boolean active = true;
}
