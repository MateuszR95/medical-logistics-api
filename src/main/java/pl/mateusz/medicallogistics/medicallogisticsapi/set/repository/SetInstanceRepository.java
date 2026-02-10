package pl.mateusz.medicallogistics.medicallogisticsapi.set.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetInstance;

/**
 * Repository interface for SetInstance entities.
 */
public interface SetInstanceRepository extends JpaRepository<SetInstance, Long> {

  /**
   * Checks if a SetInstance with the given tag ID exists.
   *
   * @param tagId the tag ID to check
   * @return true if a SetInstance with the given tag ID exists, false otherwise
   */
  boolean existsByTagId(String tagId);

  /**
   * Finds a SetInstance by its tag ID.
   *
   * @param setTagId the tag ID of the SetInstance to find
   * @return an Optional containing the found SetInstance, or empty if no
   *      SetInstance with the given tag ID exists
   */
  Optional<SetInstance> findByTagId(String setTagId);
}
