package pl.mateusz.medicallogistics.medicallogisticsapi.customer.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.medicallogistics.medicallogisticsapi.address.domain.Address;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetInstance;
import pl.mateusz.medicallogistics.medicallogisticsapi.territory.domain.Territory;
import pl.mateusz.medicallogistics.medicallogisticsapi.user.domain.User;

/**
 * Entity representing a customer in the medical logistics system.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "customer")
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 160)
  private String name;

  @Column(name = "tax_id_number", nullable = false, length = 20)
  private String taxIdNumber;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "territory_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_customer_territory"))
  private Territory territory;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "sales_rep_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_customer_sales_rep"))
  private User salesRep;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "billing_address_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_customer_billing_address"))
  private Address billingAddress;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "shipping_address_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_customer_shipping_address"))
  private Address shippingAddress;

  @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
  private List<SetInstance> setInstances = new ArrayList<>();


}
