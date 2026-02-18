package pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.domain.SetInspection;

/**
 * Repository interface for managing SetInspection entities.
 */
public interface SetInspectionRepository extends JpaRepository<SetInspection, Long> {
}
