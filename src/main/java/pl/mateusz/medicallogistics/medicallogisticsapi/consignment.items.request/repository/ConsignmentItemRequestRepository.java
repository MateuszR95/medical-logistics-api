package pl.mateusz.medicallogistics.medicallogisticsapi.consignment.items.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.consignment.items.request.domain.ConsignmentItemRequest;

/**
 * Repository interface for managing ConsignmentItemRequest entities.
 * This interface extends JpaRepository, providing CRUD operations and
 * additional query methods for ConsignmentItemRequest entities.
 */
public interface ConsignmentItemRequestRepository extends JpaRepository<
    ConsignmentItemRequest, Long> {
}
