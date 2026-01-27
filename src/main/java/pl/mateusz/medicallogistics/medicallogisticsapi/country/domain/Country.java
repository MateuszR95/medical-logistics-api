package pl.mateusz.medicallogistics.medicallogisticsapi.country.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * Entity representing a country in the medical logistics system.
 */

@Entity
@Table(name = "country",
    uniqueConstraints = @UniqueConstraint(name = "uq_country_iso2", columnNames = "iso2"))
@Getter
@Setter
@NoArgsConstructor
public class Country {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 2)
  private String iso2;

  @Column(nullable = false, length = 80)
  private String name;

  @Column(nullable = false)
  private boolean active = true;
}


