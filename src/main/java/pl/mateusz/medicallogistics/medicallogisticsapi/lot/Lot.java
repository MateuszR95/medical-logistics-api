package pl.mateusz.medicallogistics.medicallogisticsapi.lot;

import jakarta.persistence.*;

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
        name = "unique_lot_number_per_item",
        columnNames = {"lot_number", "item_id"}
    )
)
public class Lot {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "lot_number", nullable = false)
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
