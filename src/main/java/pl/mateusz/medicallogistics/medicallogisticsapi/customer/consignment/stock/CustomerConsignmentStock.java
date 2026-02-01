package pl.mateusz.medicallogistics.medicallogisticsapi.customer.consignment.stock;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.medicallogistics.medicallogisticsapi.customer.domain.Customer;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.domain.Item;

/**
 * Entity representing the consignment stock of a customer.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    name = "customer_consignment_stock",
    uniqueConstraints = {
      @UniqueConstraint(
        name = "uq_ccs_customer_item",
        columnNames = {"customer_id", "item_id"})},
    indexes = {
      @Index(name = "ix_ccs_customer", columnList = "customer_id"),
      @Index(name = "ix_ccs_item", columnList = "item_id")}
)
public class CustomerConsignmentStock {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "customer_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_ccs_customer"))
  private Customer customer;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "item_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_ccs_item"))
  private Item item;

  @Column(name = "qty", nullable = false)
  private long qty;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
