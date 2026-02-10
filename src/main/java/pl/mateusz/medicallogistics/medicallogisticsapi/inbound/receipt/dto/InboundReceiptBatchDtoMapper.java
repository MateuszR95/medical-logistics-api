package pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.InboundReceiptBatchStatus;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.domain.InboundReceiptBatch;
import pl.mateusz.medicallogistics.medicallogisticsapi.user.domain.User;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.domain.Warehouse;


/**
 * Mapper class for converting between InboundReceiptBatchDto and InboundReceiptBatch entities.
 */
public class InboundReceiptBatchDtoMapper {

  /**
   * Maps an InboundReceiptBatchDto to an InboundReceiptBatch entity.
   *
   * @param dto the data transfer object containing the batch information
   * @param receivedToWarehouse the warehouse to which the batch is received
   * @param createdBy the user who created the batch
   * @return an InboundReceiptBatch entity populated with the data from the DTO
   */
  public static InboundReceiptBatch mapToEntity(InboundReceiptBatchDto dto,
                                                Warehouse receivedToWarehouse, User createdBy) {
    InboundReceiptBatch batch = new InboundReceiptBatch();
    batch.setBatchNumber(dto.getBatchNumber());
    batch.setFileName(dto.getFileName());
    batch.setSourceWarehouseCode(dto.getSourceWarehouseCode());
    batch.setReceivedToWarehouse(receivedToWarehouse);
    batch.setStatus(InboundReceiptBatchStatus.valueOf(dto.getStatus()));
    batch.setCreatedBy(createdBy);
    batch.setCreatedAt(LocalDateTime.now(ZoneId.of("Europe/Warsaw")));
    batch.setComment(dto.getComment());
    batch.setPostedBy(null);
    batch.setPostedAt(null);
    return batch;
  }

  /**
   * Maps an InboundReceiptBatch entity to an InboundReceiptBatchDto.
   *
   * @param batch the entity containing the batch information
   * @return an InboundReceiptBatchDto populated with the data from the entity
   */
  public static InboundReceiptBatchDto mapToDto(InboundReceiptBatch batch) {
    InboundReceiptBatchDto dto = new InboundReceiptBatchDto();
    dto.setId(batch.getId());
    dto.setBatchNumber(batch.getBatchNumber());
    dto.setFileName(batch.getFileName());
    dto.setSourceWarehouseCode(batch.getSourceWarehouseCode());
    dto.setReceivedToWarehouseId(batch.getReceivedToWarehouse().getId());
    dto.setStatus(batch.getStatus().name());
    dto.setCreatedById(batch.getCreatedBy().getId());
    dto.setComment(batch.getComment());
    dto.setCreatedAt(batch.getCreatedAt());
    return dto;
  }


}
