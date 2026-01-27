package pl.mateusz.medicallogistics.medicallogisticsapi.set.domain;


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
 * Entity representing a set definition in the medical logistics system.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "set_base", uniqueConstraints = @UniqueConstraint(
    name = "uq_set_base_catalog_number",
    columnNames = "catalog_number"))
public class SetBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "catalog_number", nullable = false, length = 40)
  private String catalogNumber;

  @Column(nullable = false, length = 160)
  private String name;

  @Column(nullable = false)
  private boolean active = true;
}
