package pl.mateusz.medicallogistics.medicallogisticsapi.set.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetBase;


/** Repository interface for SetBase entities. */
public interface SetBaseRepository extends JpaRepository<SetBase, Long> {

  /** Checks if a SetBase entity exists with the given catalog number.
   *
   * @param catalogNumber the catalog number to check
   * @return true if an entity with the given catalog number exists, false otherwise
   */
  boolean existsByCatalogNumber(String catalogNumber);

  /** Finds a SetBase entity by its catalog number.
   *
   * @param catalogNumber the catalog number to search for
   * @return an Optional containing the found SetBase entity, or empty if not found
   */
  Optional<SetBase> findByCatalogNumber(String catalogNumber);
}
