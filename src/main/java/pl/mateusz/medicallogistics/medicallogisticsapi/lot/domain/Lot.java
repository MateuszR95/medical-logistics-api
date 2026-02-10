package pl.mateusz.medicallogistics.medicallogisticsapi.lot.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.domain.Item;

/**
 * Class representing a lot in the medical logistics system.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "lot",
    uniqueConstraints = @UniqueConstraint(
        name = "unique_expiration_date_per_lot_number",
        columnNames = {"lot_number", "expiration_date"}
    )
)

public class Lot {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "lot_number", nullable = false)
  @Pattern(
      regexp = "^[A-Z]{3}[0-9]{3}[A-Z]{2}$",
      message = "Lot number must match pattern: ABC123AB"
  )
  private String lotNumber;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "item_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_lot_item")
  )
  private Item item;
  @Column(name = "expiration_date")
  private LocalDate expirationDate;

}
