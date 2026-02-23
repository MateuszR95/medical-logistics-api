package pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.domain.SetInspection;


/**
 * Repository interface for managing SetInspection entities.
 */
public interface SetInspectionRepository extends JpaRepository<SetInspection, Long> {

  /**
   * Finds a SetInspection by its unique set inspection number.
   *
   * @param setInspectionNumber the unique set inspection number to search for
   * @return an Optional containing the found SetInspection, or empty if not found
   */
  Optional<SetInspection> findBySetInspectionNumber(String setInspectionNumber);
}
