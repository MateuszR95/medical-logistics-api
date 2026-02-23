package pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.dto;

import pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.domain.StockMovement;

/**
 * Mapper class for converting StockMovement entities to StockMovementDto objects.
 * This class provides a method to map the relevant fields from a StockMovement
 * entity to a StockMovementDto, which can be used for data transfer purposes.
 */
public class StockMovementDtoMapper {

  /**
   * Maps a StockMovement entity to a StockMovementDto.
   *
   * @param stockMovement the StockMovement entity to be mapped
   * @return a StockMovementDto containing the mapped data from the StockMovement entity
   */
  public static StockMovementDto mapToDto(StockMovement stockMovement) {
    return StockMovementDto.builder()
        .previousLocationCode(stockMovement.getFromLocation() != null
            ? stockMovement.getFromLocation().getCode() : null)
        .newLocationCode(stockMovement.getToLocation() != null
            ? stockMovement.getToLocation().getCode() : null)
        .itemRefNumber(stockMovement.getItem() != null
            ? stockMovement.getItem().getRefNumber() : null)
        .lotNumber(stockMovement.getLot() != null
            ? stockMovement.getLot().getLotNumber() : null)
        .quantity(stockMovement.getQty())
        .movementType(stockMovement.getMovementType().name())
        .movedByEmail(stockMovement.getPostedBy() != null
            ? stockMovement.getPostedBy().getEmail() : null)
        .build();
  }
}
