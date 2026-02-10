package pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.InboundReceiptLineStatus;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.InboundReceiptLineType;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.domain.InboundReceiptBatch;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.domain.InboundReceiptLine;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.domain.Item;
import pl.mateusz.medicallogistics.medicallogisticsapi.lot.domain.Lot;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetInstance;


/**
 * Mapper class for converting InboundReceiptLineDto to InboundReceiptLine entity.
 */
public class InboundReceiptLineDtoMapper {

  /**
   * Maps an InboundReceiptLineDto to an InboundReceiptLine entity.
   *
   * @param dto the DTO to map
   * @param batch the associated InboundReceiptBatch
   * @param resolvedItem the resolved Item entity
   * @param resolveLot the resolved Lot entity
   * @param resolvedSetInstance the resolved SetInstance entity
   * @return the mapped InboundReceiptLine entity
   */
  public static InboundReceiptLine mapToEntity(InboundReceiptLineDto dto, InboundReceiptBatch batch,
                                               Item resolvedItem, Lot resolveLot,
                                               SetInstance resolvedSetInstance) {
    InboundReceiptLine inboundReceiptLine = new InboundReceiptLine();
    inboundReceiptLine.setBatch(batch);
    inboundReceiptLine.setItemRefNumber(dto.getItemRefNumber());
    inboundReceiptLine.setLotNumber(dto.getLotNumber());
    LocalDate expiration = (dto.getExpirationDate() == null || dto.getExpirationDate().isBlank())
        ? null
        : LocalDate.parse(dto.getExpirationDate());
    inboundReceiptLine.setExpirationDate(expiration);
    inboundReceiptLine.setQty(dto.getQty());
    inboundReceiptLine.setResolvedItem(resolvedItem);
    inboundReceiptLine.setResolvedLot(resolveLot);
    inboundReceiptLine.setStatus(InboundReceiptLineStatus.OK);
    inboundReceiptLine.setLineType(InboundReceiptLineType.valueOf(dto.getLineType()));
    inboundReceiptLine.setResolvedSetInstance(resolvedSetInstance);
    inboundReceiptLine.setSetCatalogNumber(dto.getSetCatalogNumber());
    inboundReceiptLine.setSetTagId(dto.getSetTagId());
    inboundReceiptLine.setErrorMessage(null);
    return inboundReceiptLine;
  }

  /**
   * Maps an InboundReceiptLine entity to an InboundReceiptLineDto.
   *
   * @param entity the entity to map
   * @return the mapped InboundReceiptLineDto
   */
  public static InboundReceiptLineDto mapToDto(InboundReceiptLine entity) {
    InboundReceiptLineDto dto = new InboundReceiptLineDto();
    dto.setLineType(entity.getLineType().name());
    dto.setItemRefNumber(entity.getItemRefNumber());
    dto.setLotNumber(entity.getLotNumber());
    dto.setExpirationDate(entity.getExpirationDate() != null
        ? entity.getExpirationDate().format(DateTimeFormatter.ISO_LOCAL_DATE)
        : null);
    dto.setQty(entity.getQty());
    dto.setSetCatalogNumber(entity.getSetCatalogNumber());
    dto.setSetTagId(entity.getSetTagId());
    return dto;
  }

}
