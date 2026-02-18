package pl.mateusz.medicallogistics.medicallogisticsapi.set.returns.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mateusz.medicallogistics.medicallogisticsapi.exception.ResourceNotFoundException;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.domain.InboundReceiptBatch;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.domain.InboundReceiptLine;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.repository.InboundReceiptBatchRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.repository.InboundReceiptLineRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetInstance;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.returns.domain.SetReceipt;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.returns.repository.SetReceiptRepository;

/**
 * Service class for managing set receipts, which represent the return of medical sets.
 * This service provides methods to handle the business logic related to set receipts,
 * such as creating new receipts, retrieving existing receipts, and updating receipt information.
 */
@Service
public class SetReceiptService {

  private final SetReceiptRepository setReceiptRepository;
  private final InboundReceiptLineRepository inboundReceiptLineRepository;
  private final InboundReceiptBatchRepository inboundReceiptBatchRepository;

  /**
   * Constructs a new SetReceiptService with the specified dependencies.
   *
   * @param setReceiptRepository the repository for managing SetReceipt entities
   * @param inboundReceiptLineRepository the repository for managing InboundReceiptLine entities
   * @param inboundReceiptBatchRepository the repository for managing InboundReceiptBatch entities
   */
  public SetReceiptService(SetReceiptRepository setReceiptRepository,
                           InboundReceiptLineRepository inboundReceiptLineRepository,
                           InboundReceiptBatchRepository inboundReceiptBatchRepository) {
    this.setReceiptRepository = setReceiptRepository;
    this.inboundReceiptLineRepository = inboundReceiptLineRepository;
    this.inboundReceiptBatchRepository = inboundReceiptBatchRepository;
  }

  /**
   * Creates and saves a new SetReceipt based on the provided SetInstance and InboundReceiptBatch.
   * This method generates a unique receipt number, sets the received date and
   * user based on the inbound batch,
   * and saves the new SetReceipt to the database.
   *
   * @param setInstance The SetInstance associated with the receipt.
   * @param inboundBatch The InboundReceiptBatch containing information about the receipt.
   */
  @Transactional
  public void createAndSaveSetReceiptFromInboundReceipt(SetInstance setInstance,
                                            InboundReceiptBatch inboundBatch) {
    SetReceipt setReceipt = new SetReceipt();
    setReceipt.setReceiptNumber(generateReceiptNumber());
    setReceipt.setSetInstance(setInstance);
    setReceipt.setReceivedAt(inboundBatch.getCreatedAt());
    setReceipt.setReceivedBy(inboundBatch.getCreatedBy());
    setReceipt.setComment("Set receipt created from inbound receipt batch: "
        + inboundBatch.getBatchNumber());
    setReceiptRepository.save(setReceipt);
  }

  private String generateReceiptNumber() {
    return "SR-" + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter
      .ofPattern("yyyyMMdd-HHmm"))
      + "-" + java.util.UUID.randomUUID().toString().substring(0, 6).toUpperCase();
  }
}
