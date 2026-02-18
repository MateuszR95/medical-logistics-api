package pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.domain.SetInspectionDiscrepancyLine;

/**
 * Repository interface for managing SetInspectionDiscrepancyLine entities.
 * This repository provides basic CRUD operations and can be extended with custom queries if needed.
 */
public interface SetInspectionDiscrepancyLineRepository extends JpaRepository<
    SetInspectionDiscrepancyLine, Long> {
}
