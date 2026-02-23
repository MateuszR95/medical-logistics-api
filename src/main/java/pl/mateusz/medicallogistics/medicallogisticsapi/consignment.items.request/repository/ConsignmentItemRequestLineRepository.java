package pl.mateusz.medicallogistics.medicallogisticsapi.consignment.items.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.consignment.items.request.domain.ConsignmentItemRequestLine;

/**
 * Repository interface for managing ConsignmentItemRequestLine entities.
 * Provides CRUD operations and query methods for ConsignmentItemRequestLine.
 */
public interface ConsignmentItemRequestLineRepository extends JpaRepository<
    ConsignmentItemRequestLine, Long> {
}
