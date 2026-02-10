package pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.domain.InboundReceiptLine;

/**
 * Repository interface for managing InboundReceiptLine entities.
 * Extends JpaRepository to provide CRUD operations and database interaction
 * for the InboundReceiptLine entity.
 */
public interface InboundReceiptLineRepository extends JpaRepository<InboundReceiptLine, Long> {

  /**
   * Finds an InboundReceiptLine by its associated batch ID.
   *
   * @param batchId the ID of the batch to search for
   * @return an Optional containing the found InboundReceiptLine, or empty if not found
   */
  Optional<InboundReceiptLine> findByBatchId(long batchId);
}
