package pl.mateusz.medicallogistics.medicallogisticsapi.item.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.ItemType;
import pl.mateusz.medicallogistics.medicallogisticsapi.lot.Lot;


/**
 * Entity representing an item in the medical logistics system.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "item",
    uniqueConstraints = @UniqueConstraint(
      name = "unique_ref_number",
      columnNames = {"ref_number"}))
public class Item {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false, length = 100)
  private String name;
  @Column(name = "ref_number", nullable = false)
  private String refNumber;
  @Enumerated(value = EnumType.STRING)
  @Column(name = "item_type", nullable = false)
  private ItemType itemType;
  @Column(name = "sterile", nullable = false)
  private boolean sterile;
  @OneToMany(mappedBy = "item")
  private List<Lot> lots = new ArrayList<>();

}
