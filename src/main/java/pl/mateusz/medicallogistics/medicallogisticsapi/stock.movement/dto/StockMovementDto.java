package pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for representing stock movement information.
 * This DTO is used to transfer data related to stock movements between
 * different layers of the application, such as from the service layer to
 * the presentation layer or for API responses.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockMovementDto {

  private String previousLocationCode;
  private String newLocationCode;
  private String itemRefNumber;
  private String lotNumber;
  private long quantity;
  private String movementType;
  private String movedByEmail;


}
