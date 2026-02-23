package pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mateusz.medicallogistics.medicallogisticsapi.exception.ResourceNotFoundException;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetInstance;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.SetInspectionDiscrepancyResolution;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.SetInspectionDiscrepancyType;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.domain.SetInspectionDiscrepancyLine;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.repository.SetInspectionDiscrepancyLineRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.domain.SetInspection;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.repository.SetInspectionRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.StockMovementRefType;
import pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.StockMovementType;
import pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.domain.StockMovement;
import pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.dto.StockMovementDto;
import pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.dto.StockMovementDtoMapper;
import pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.repository.StockMovementRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.user.domain.User;
import pl.mateusz.medicallogistics.medicallogisticsapi.user.repository.UserRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.domain.Location;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.repository.LocationRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.service.InventoryService;

/**
 * Service class for managing StockMovement entities.
 * Provides business logic and operations related to StockMovement.
 */
@Service
public class StockMovementService {

  private final StockMovementRepository stockMovementRepository;
  private final SetInspectionRepository setInspectionRepository;
  private final LocationRepository locationRepository;
  private final UserRepository userRepository;
  private final SetInspectionDiscrepancyLineRepository setInspectionDiscrepancyLineRepository;

  private final InventoryService inventoryService;
  private static final String LOCATION_CODE_FOR_OVERAGES  = "QUAR-RET";


  /**
   * Constructs a new StockMovementService with the specified dependencies.
   *
   * @param stockMovementRepository the repository for managing StockMovement entities
   * @param setInspectionRepository the repository for managing SetInspection entities
   * @param locationRepository the repository for managing Location entities
   * @param userRepository the repository for managing User entities
   * @param setInspectionDiscrepancyLineRepository the repository for managing
   *                                              SetInspectionDiscrepancyLine entities
   * @param inventoryService the service for managing inventory operations
   */
  public StockMovementService(StockMovementRepository stockMovementRepository,
               SetInspectionRepository setInspectionRepository,
               LocationRepository locationRepository,
               UserRepository userRepository,
               SetInspectionDiscrepancyLineRepository
               setInspectionDiscrepancyLineRepository, InventoryService inventoryService) {
    this.stockMovementRepository = stockMovementRepository;
    this.setInspectionRepository = setInspectionRepository;
    this.locationRepository = locationRepository;
    this.userRepository = userRepository;
    this.setInspectionDiscrepancyLineRepository = setInspectionDiscrepancyLineRepository;
    this.inventoryService = inventoryService;
  }

  /**
   * Processes overage discrepancies from a set inspection and moves the overage
   * parts to quarantine.
   * This method creates stock movements for each overage discrepancy line, updates the inventory,
   * and changes the resolution of the discrepancy lines to "QUARANTINE".
   *
   * @param setInspectionNumber the inspection number of the set inspection to process
   * @param comment an optional comment to be associated with the stock movements
   * @param userEmail the email of the user performing the operation
   * @return a list of StockMovementDto representing the stock movements that were created
   * @throws ResourceNotFoundException if the set inspection, location for overages,
   *      or user is not found
   */

  @Transactional
  public List<StockMovementDto> processOveragesToQuarantine(
      String setInspectionNumber, String comment, String userEmail) {

    List<StockMovementDto> stockMovements = new ArrayList<>();
    SetInspection setInspection = setInspectionRepository
        .findBySetInspectionNumber(setInspectionNumber)
        .orElseThrow(() -> new ResourceNotFoundException(
        "SetInspection with number " + setInspectionNumber + " not found"));
    Location locationForOverages = locationRepository.findByCode(LOCATION_CODE_FOR_OVERAGES)
        .orElseThrow(() -> new ResourceNotFoundException(
        "Location with code " + LOCATION_CODE_FOR_OVERAGES + " not found"));
    User movedBy = userRepository.findByEmailAndActiveTrue(userEmail)
        .orElseThrow(() -> new ResourceNotFoundException(
        "Active user with email " + userEmail + " not found"));
    SetInstance inspectedSet = setInspection.getSetReceipt().getSetInstance();
    List<SetInspectionDiscrepancyLine> discrepancyLines = setInspection.getDiscrepancyLines();
    for (SetInspectionDiscrepancyLine discrepancyLine : discrepancyLines) {
      if (discrepancyLine.getDiscrepancyType() == SetInspectionDiscrepancyType.OVERAGE
          && discrepancyLine.getResolution().equals(SetInspectionDiscrepancyResolution.PENDING)) {
        StockMovement stockMovement = createStockMovementFromSetReceipt(comment,
            discrepancyLine, inspectedSet, locationForOverages, movedBy, setInspection);
        StockMovement savedStockMovement = stockMovementRepository.save(stockMovement);
        stockMovements.add(StockMovementDtoMapper.mapToDto(savedStockMovement));
        inventoryService.addOveragePartsAfterSetInspectionToInventory(savedStockMovement);
        discrepancyLine.setResolution(SetInspectionDiscrepancyResolution.QUARANTINE);
        setInspectionDiscrepancyLineRepository.save(discrepancyLine);
      }
    }
    return stockMovements;
  }

  private static StockMovement createStockMovementFromSetReceipt(String comment,
                        SetInspectionDiscrepancyLine discrepancyLine,
                        SetInstance inspectedSet, Location locationForOverages,
                        User movedBy, SetInspection setInspection) {
    StockMovement stockMovement = new StockMovement();
    stockMovement.setMovementType(StockMovementType.ADJUSTMENT);
    stockMovement.setFromLocation(inspectedSet.getLocation());
    stockMovement.setToLocation(locationForOverages);
    stockMovement.setItem(discrepancyLine.getItem());
    stockMovement.setLot(discrepancyLine.getLot());
    stockMovement.setQty(discrepancyLine.getQty());
    stockMovement.setPostedBy(movedBy);
    stockMovement.setPostedAt(LocalDateTime.now());
    stockMovement.setRefType(StockMovementRefType.SET_INSPECTION);
    stockMovement.setRefId(setInspection.getId());
    if (comment != null && !comment.trim().isEmpty()) {
      stockMovement.setComment(comment);
    }
    return stockMovement;
  }
}

