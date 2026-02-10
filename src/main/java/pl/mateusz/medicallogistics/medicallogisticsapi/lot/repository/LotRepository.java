package pl.mateusz.medicallogistics.medicallogisticsapi.lot.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.lot.domain.Lot;



/**
 * Repository interface for managing Lot entities.
 * This interface extends JpaRepository to provide CRUD operations
 * and query methods for Lot entities.
 */
public interface LotRepository extends JpaRepository<Lot, Long> {

  /**
   * Finds a Lot by its lot number.
   *
   * @param lotNumber the lot number to search for
   * @return an Optional containing the found Lot, or empty if no Lot with
   *      the given lot number exists
   */
  Optional<Lot> findByLotNumber(String lotNumber);

}
