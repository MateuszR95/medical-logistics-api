package pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.domain.Warehouse;


/**
 * Repository interface for managing Warehouse entities.
 */
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

  /**
   * Finds a warehouse by its unique code.
   *
   * @param code the unique code of the warehouse
   * @return an Optional containing the found Warehouse, or empty if not found
   */
  Optional<Warehouse> findByCode(String code);
}
