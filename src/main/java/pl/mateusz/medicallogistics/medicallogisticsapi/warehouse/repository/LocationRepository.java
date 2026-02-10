package pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.domain.Location;

/**
 * Repository interface for managing Location entities in the medical logistics system.
 * This repository provides methods for performing CRUD operations and custom queries
 * related to Location entities, which represent specific locations within a warehouse.
 */
public interface LocationRepository extends JpaRepository<Location, Long> {

  /**
   * Finds a Location entity by its unique code.
   *
   * @param code the unique code of the location to find
   * @return an Optional containing the found Location, or empty
   *      if no Location with the given code exists
   */
  Optional<Location> findByCode(String code);

  /**
   * Finds a Location entity by the warehouse ID and location code.
   *
   * @param id the ID of the warehouse to which the location belongs
   * @param defaultReceiptLocationCode the unique code of the location to find
   * @return an Optional containing the found Location, or empty if no
   *      Location with the given warehouse ID and code exists
   */
  Optional<Location> findByWarehouseIdAndCode(Long id, String defaultReceiptLocationCode);
}
