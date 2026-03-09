package pl.mateusz.medicallogistics.medicallogisticsapi.consignment.items.request.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.consignment.items.request.domain.ConsignmentItemRequest;


/**
 * Repository interface for managing ConsignmentItemRequest entities.
 * This interface extends JpaRepository, providing CRUD operations and
 * additional query methods for ConsignmentItemRequest entities.
 */
public interface ConsignmentItemRequestRepository extends JpaRepository<
    ConsignmentItemRequest, Long> {

  /**
   * Finds a ConsignmentItemRequest by its source reference.
   *
   * @param sourceRef the source reference to search for
   * @return an Optional containing the found ConsignmentItemRequest, or empty if not found
   */
  Optional<ConsignmentItemRequest> findBySourceRef(String sourceRef);
}
