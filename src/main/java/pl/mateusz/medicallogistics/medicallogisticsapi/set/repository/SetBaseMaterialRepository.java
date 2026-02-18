package pl.mateusz.medicallogistics.medicallogisticsapi.set.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetBaseMaterial;

/**
 * Repository interface for managing SetBaseMaterial entities.
 * This repository provides CRUD operations and query methods for SetBaseMaterial.
 */
public interface SetBaseMaterialRepository extends JpaRepository<SetBaseMaterial, Long> {

  /**
   * Finds a SetBaseMaterial by its catalog number.
   *
   * @param catalogNumber the catalog number of the SetBaseMaterial to find
   * @return an Optional containing the found SetBaseMaterial, or empty if not found
   */
  List<SetBaseMaterial> findBySetBaseCatalogNumber(String catalogNumber);

}
