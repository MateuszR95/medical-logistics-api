package pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.service;

import org.springframework.stereotype.Service;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.domain.InboundReceiptBatch;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.repository.InboundReceiptBatchRepository;

/**
 * Service for managing inbound receipt batches.
 * This service is responsible for handling business logic related to inbound receipt batches,
 * such as creating, updating, and retrieving batches of inbound receipts.
 */
@Service
public class InboundReceiptBatchService {

  private final InboundReceiptBatchRepository inboundReceiptBatchRepository;

  /**
   * Constructs an InboundReceiptBatchService with the specified repository.
   *
   * @param inboundReceiptBatchRepository the repository for managing InboundReceiptBatch entities
   */
  public InboundReceiptBatchService(InboundReceiptBatchRepository inboundReceiptBatchRepository) {
    this.inboundReceiptBatchRepository = inboundReceiptBatchRepository;
  }

  /**
   * Retrieves an inbound receipt batch by its file name.
   *
   * @param fileName the name of the file associated with the inbound receipt batch
   * @return the InboundReceiptBatch entity corresponding to the specified file name
   * @throws IllegalStateException if no batch is found for the given file name
   */
  public InboundReceiptBatch getInboundReceiptBatchByFileName(String fileName) {
    return inboundReceiptBatchRepository
        .findByFileName(fileName)
        .orElseThrow(() -> new IllegalStateException("Batch not found for file: "
        + fileName));
  }
}
