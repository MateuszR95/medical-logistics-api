package pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.domain.InboundReceiptBatch;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.domain.InboundReceiptLine;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.dto.InboundReceiptLineDto;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.dto.InboundReceiptLineDtoMapper;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.repository.InboundReceiptLineRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.domain.Item;
import pl.mateusz.medicallogistics.medicallogisticsapi.lot.domain.Lot;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetInstance;

/**
 * Service for managing inbound receipt lines.
 * This service is responsible for handling business logic related to inbound receipt lines,
 * such as creating, updating, and retrieving line items associated with inbound receipts.
 */
@Service
public class InboundReceiptLineService {

  private final InboundReceiptLineRepository inboundReceiptLineRepository;

  /**
   * Constructs an InboundReceiptLineService with the specified repository.
   *
   * @param inboundReceiptLineRepository the repository for managing InboundReceiptLine entities
   */
  public InboundReceiptLineService(InboundReceiptLineRepository inboundReceiptLineRepository) {
    this.inboundReceiptLineRepository = inboundReceiptLineRepository;
  }

  /**
   * Saves an inbound receipt line based on the provided DTO and associated batch.
   *
   * @param lineDto the DTO containing the details of the inbound receipt line to be saved
   * @param batch the associated InboundReceiptBatch to which the line belongs
   * @return the saved InboundReceiptLineDto representing the persisted line item
   */
  @Transactional
  public InboundReceiptLineDto save(InboundReceiptLineDto lineDto,
                                    InboundReceiptBatch batch, Item resolveItem, Lot resolvedLot,
                                    SetInstance resolvedSet) {
    InboundReceiptLine inboundReceiptLine = InboundReceiptLineDtoMapper.mapToEntity(
        lineDto, batch, resolveItem, resolvedLot, resolvedSet);
    InboundReceiptLine savedInboundReceiptLine = inboundReceiptLineRepository
        .save(inboundReceiptLine);
    return InboundReceiptLineDtoMapper.mapToDto(savedInboundReceiptLine);
  }
}
