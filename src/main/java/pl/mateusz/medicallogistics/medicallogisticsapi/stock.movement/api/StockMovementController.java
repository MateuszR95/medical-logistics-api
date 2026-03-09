package pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.api;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.dto.StockMovementDto;
import pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.service.StockMovementService;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.service.InventoryService;

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
   * @param stockMovementService the service for managing stock movements, injected via constructor
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


  /**
   * Endpoint to move damaged parts from a set to inventory. This endpoint
   * accepts the set inspection
   * number, an optional comment, and the authentication object containing details
   * of the authenticated user.
   *
   * @param setInspectionNumber the inspection number of the set from which to move damaged parts
   * @param comment an optional comment to be associated with the stock movement
   * @param authentication the authentication object containing details of the authenticated user
   * @return a ResponseEntity containing a list of StockMovementDto representing the stock movements
   *      that were created as a result of moving damaged parts to inventory
   */
  @PostMapping("/process-damaged-parts/{setInspectionNumber}")
  public ResponseEntity<List<StockMovementDto>> moveDamagedPartsFromSetToInventory(
      @PathVariable String setInspectionNumber,
      @RequestParam(required = false) String comment,
      Authentication authentication) {
    List<StockMovementDto> damagedItemsToQuarantine = stockMovementService
        .moveDamagedItemsToQuarantine(setInspectionNumber, comment,
        authentication.getName());
    return ResponseEntity.ok(damagedItemsToQuarantine);
  }


  /**
   * Endpoint to retrieve details of items that need to be replenished for a set after inspection.
   * This endpoint accepts the set inspection number and the authentication object containing
   * details of the authenticated user.
   *
   * @param setInspectionNumber the inspection number of the set for which
   *                            to retrieve replenishment details
   * @param authentication the authentication object containing details of the authenticated user
   * @return a ResponseEntity containing a list of StockMovementDto representing
   *      the details of items
   *      that need to be replenished for the set after inspection
   */
  @GetMapping("/get-items-to-replenishments/{setInspectionNumber}")
  public ResponseEntity<List<StockMovementDto>> getPartsDetailsToReplenishSetAfterInspection(
      @PathVariable String setInspectionNumber, Authentication authentication) {
    List<StockMovementDto> stockMovementDtoList = stockMovementService
        .getItemsDetailsForSetReplenishmentAfterInspection(setInspectionNumber);
    return ResponseEntity.ok(stockMovementDtoList);
  }

  /**
   * Endpoint to move items to replenish a set after inspection. This endpoint
   * accepts the set tag ID and the authentication object containing details
   *  of the authenticated user.
   *
   * @param setTagId the tag ID of the set for which to move items for replenishment
   *                after inspection
   * @param authentication the authentication object containing details of the authenticated user
   * @return a ResponseEntity containing a list of StockMovementDto representing the stock movements
   *      that were created as a result of moving items to replenish the set after inspection
   */
  @PostMapping("/process-set-replenishments/{setTagId}")
  public ResponseEntity<List<StockMovementDto>> replenishSetAfterInspection(
                              @PathVariable String setTagId,
                              Authentication authentication) {
    List<StockMovementDto> movementDtoList = stockMovementService
        .moveItemsToReplenishSetByTagId(setTagId, authentication.getName());
    return ResponseEntity.ok(movementDtoList);

  }
}
