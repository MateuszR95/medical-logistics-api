package pl.mateusz.medicallogistics.medicallogisticsapi.address.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing an address in the medical logistics system.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "address")
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "line1", nullable = false, length = 120)
  private String line1;

  @Column(name = "line2", length = 120)
  private String line2;

  @Column(name = "city", nullable = false, length = 80)
  private String city;

  @Column(name = "postal_code", nullable = false, length = 20)
  private String postalCode;

  @Column(name = "country_code", nullable = false, length = 2)
  private String countryCode;

  @Column(name = "company_name", length = 160)
  private String companyName;

  @Column(name = "contact_name", length = 120)
  private String contactName;

  @Column(name = "phone", length = 40)
  private String phone;

  @Column(name = "email", length = 80)
  private String email;

}
