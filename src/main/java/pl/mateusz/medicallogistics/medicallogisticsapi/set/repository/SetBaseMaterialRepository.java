package pl.mateusz.medicallogistics.medicallogisticsapi.set.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetBaseMaterial;


/**
 * Repository interface for managing SetBaseMaterial entities.
 * This repository provides CRUD operations and query methods for SetBaseMaterial.
 */
public interface SetBaseMaterialRepository extends JpaRepository<SetBaseMaterial, Long> {

  /**
   * Finds a SetBaseMaterial by its associated SetBase's catalog number.
   *
   * @param catalogNumber the catalog number of the associated SetBase
   * @return an Optional containing the found SetBaseMaterial, or empty if not found
   */
  Optional<SetBaseMaterial> findBySetBaseCatalogNumber(String catalogNumber);
}
