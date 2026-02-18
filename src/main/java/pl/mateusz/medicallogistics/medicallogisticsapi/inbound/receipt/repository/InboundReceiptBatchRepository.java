package pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.domain.InboundReceiptBatch;


/**
 * Repository interface for managing InboundReceiptBatch entities.
 * Extends JpaRepository to provide CRUD operations and database interaction
 * for the InboundReceiptBatch entity.
 */
public interface InboundReceiptBatchRepository extends JpaRepository<InboundReceiptBatch, Long> {

  /**
   * Finds an InboundReceiptBatch by its batch number.
   *
   * @param batchNumber the batch number to search for
   * @return an Optional containing the found InboundReceiptBatch, or empty if not found
   */
  Optional<InboundReceiptBatch> findByBatchNumber(String batchNumber);

  /**
   * Finds an InboundReceiptBatch by its file name.
   *
   * @param fileName the file name to search for
   * @return an Optional containing the found InboundReceiptBatch, or empty if not found
   */
  Optional<InboundReceiptBatch> findByFileName(String fileName);

  /**
   * Finds a distinct InboundReceiptBatch that contains a line with the specified line ID.
   *
   * @param lineId the ID of the line to search for
   * @return an Optional containing the found InboundReceiptBatch, or empty if not found
   */
  Optional<InboundReceiptBatch> findDistinctByLinesId(Long lineId);


}
