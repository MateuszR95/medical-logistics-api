package pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.api;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.dto.StockMovementDto;
import pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.service.StockMovementService;

/**
 * REST controller for managing stock movements.
 * This controller provides endpoints for creating and retrieving stock movements.
 */
@RestController
@RequestMapping("/api/stock-movements")
public class StockMovementController {

  private final StockMovementService stockMovementService;

  /**
   * Constructs a new StockMovementController with the specified StockMovementService.
   *
   * @param stockMovementService the service for managing stock movements
   */
  public StockMovementController(StockMovementService stockMovementService) {
    this.stockMovementService = stockMovementService;
  }

  /**
   * Endpoint to move overage parts from a set to inventory. This endpoint
   * accepts the set inspection
   * number, an optional comment, and the authentication object containing details
   * of the authenticated user.
   *
   * @param setInspectionNumber the inspection number of the set from which to move overage parts
   * @param comment an optional comment to be associated with the stock movement
   * @param authentication the authentication object containing details of the authenticated user
   * @return a ResponseEntity containing a list of StockMovementDto representing the stock movements
   *      that were created as a result of moving overage parts to inventory
   */
  @PostMapping("/process-overage-parts/{setInspectionNumber}")
  public ResponseEntity<List<StockMovementDto>> moveOveragePartsFromSetToInventory(
      @PathVariable String setInspectionNumber,
      @RequestParam(required = false) String comment,
      Authentication authentication) {
    List<StockMovementDto> stockMovements = stockMovementService.processOveragesToQuarantine(
        setInspectionNumber, comment, authentication.getName());
    return ResponseEntity.ok(stockMovements);
  }
}
