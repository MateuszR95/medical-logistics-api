package pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mateusz.medicallogistics.medicallogisticsapi.exception.ResourceNotFoundException;
import pl.mateusz.medicallogistics.medicallogisticsapi.exception.UnauthorizedException;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetInstance;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetInstanceMaterial;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.SetInspectionDiscrepancyResolution;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.SetInspectionDiscrepancyType;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.domain.SetInspectionDiscrepancyLine;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.repository.SetInspectionDiscrepancyLineRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.domain.SetInspection;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.repository.SetInspectionRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.repository.SetInstanceMaterialRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.repository.SetInstanceRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.StockMovementRefType;
import pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.StockMovementStatus;
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

  private final SetInstanceMaterialRepository setInstanceMaterialRepository;

  private final SetInstanceRepository setInstanceRepository;
  private final InventoryService inventoryService;
  private static final String LOCATION_CODE_FOR_OVERAGES  = "QUAR-RET";
  private static final String LOCATION_CODE_FOR_DAMAGED  = "QUAR-IN";


  /**
   * Constructs a new StockMovementService with the specified dependencies.
   *
   * @param stockMovementRepository the repository for managing StockMovement entities
   * @param setInspectionRepository the repository for managing SetInspection entities
   * @param locationRepository the repository for managing Location entities
   * @param userRepository the repository for managing User entities
   * @param setInspectionDiscrepancyLineRepository the repository for managing
   *                                              SetInspectionDiscrepancyLine entities
   * @param setInstanceMaterialRepository the repository for managing SetInstanceMaterial entities
   * @param setInstanceRepository the repository for managing SetInstance entities
   * @param inventoryService the service for managing inventory operations
   */
  public StockMovementService(StockMovementRepository stockMovementRepository,
                              SetInspectionRepository setInspectionRepository,
                              LocationRepository locationRepository,
                              UserRepository userRepository,
                              SetInspectionDiscrepancyLineRepository
                                setInspectionDiscrepancyLineRepository,
                              SetInstanceMaterialRepository setInstanceMaterialRepository,
                              SetInstanceRepository setInstanceRepository,
                              InventoryService inventoryService) {
    this.stockMovementRepository = stockMovementRepository;
    this.setInspectionRepository = setInspectionRepository;
    this.locationRepository = locationRepository;
    this.userRepository = userRepository;
    this.setInspectionDiscrepancyLineRepository = setInspectionDiscrepancyLineRepository;
    this.setInstanceMaterialRepository = setInstanceMaterialRepository;
    this.setInstanceRepository = setInstanceRepository;
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
        .orElseThrow(() -> new UnauthorizedException("User with email " + userEmail
        + " not found or inactive."));
    SetInstance inspectedSet = setInspection.getSetReceipt().getSetInstance();
    List<SetInspectionDiscrepancyLine> discrepancyLines = setInspection.getDiscrepancyLines();
    for (SetInspectionDiscrepancyLine discrepancyLine : discrepancyLines) {
      if (discrepancyLine.getDiscrepancyType() == SetInspectionDiscrepancyType.OVERAGE
          && discrepancyLine.getResolution().equals(SetInspectionDiscrepancyResolution.PENDING)) {
        StockMovement savedStockMovement = createStockMovementFromSetReceipt(comment,
            discrepancyLine, inspectedSet, locationForOverages, movedBy, setInspection);
        stockMovements.add(StockMovementDtoMapper.mapToDto(savedStockMovement));
        inventoryService.addOveragePartsAfterSetInspectionToInventory(savedStockMovement);
        discrepancyLine.setResolution(SetInspectionDiscrepancyResolution.QUARANTINE);
        setInspectionDiscrepancyLineRepository.save(discrepancyLine);
      }
    }
    return stockMovements;
  }

  /**
   * Moves damaged items from a set inspection to quarantine. This method creates stock movements
   * for each damaged item, updates the inventory, and changes the resolution
   * of the discrepancy lines
   * to "QUARANTINE".
   *
   * @param setInspectionNumber the inspection number of the set inspection to process
   * @param comment an optional comment to be associated with the stock movements
   * @param userEmail the email of the user performing the operation
   * @return a list of StockMovementDto representing the stock movements that were created
   * @throws ResourceNotFoundException if the set inspection, location for damaged items,
   *      or user is not found
   */
  @Transactional
  public List<StockMovementDto> moveDamagedItemsToQuarantine(
      String setInspectionNumber, String comment, String userEmail) {

    List<StockMovementDto> stockMovements = new ArrayList<>();
    SetInspection setInspection = setInspectionRepository
        .findBySetInspectionNumber(setInspectionNumber)
        .orElseThrow(() -> new ResourceNotFoundException(
        "SetInspection with number " + setInspectionNumber + " not found"));

    Location locationForDamagedItems = locationRepository.findByCode(LOCATION_CODE_FOR_DAMAGED
        .trim())
        .orElseThrow(() -> new ResourceNotFoundException(
        "Location with code: " + LOCATION_CODE_FOR_DAMAGED + " not found"));

    User movedBy = userRepository.findByEmailAndActiveTrue(userEmail)
        .orElseThrow(() -> new UnauthorizedException("User with email " + userEmail
        + " not found or inactive."));

    SetInstance inspectedSet = setInspection.getSetReceipt().getSetInstance();

    List<SetInspectionDiscrepancyLine> damagedPartsList = setInspection.getDiscrepancyLines()
        .stream()
        .filter(line -> line.getDiscrepancyType() == SetInspectionDiscrepancyType.DAMAGED)
        .filter(line -> line.getResolution() == SetInspectionDiscrepancyResolution.PENDING)
        .toList();

    for (SetInspectionDiscrepancyLine discrepancyLine : damagedPartsList) {
      StockMovement stockMovement = createDamagedItemQuarantineMovement(
          comment, discrepancyLine, inspectedSet, locationForDamagedItems, movedBy, setInspection);

      StockMovement savedStockMovement = stockMovementRepository.save(stockMovement);
      stockMovements.add(StockMovementDtoMapper.mapToDto(savedStockMovement));

      inventoryService.updateInventoryForDamagedPartsAfterInspection(savedStockMovement);

      discrepancyLine.setResolution(SetInspectionDiscrepancyResolution.QUARANTINE);
      setInspectionDiscrepancyLineRepository.save(discrepancyLine);
    }

    return stockMovements;
  }

  private StockMovement createDamagedItemQuarantineMovement(
        String comment,
        SetInspectionDiscrepancyLine discrepancyLine,
        SetInstance inspectedSet,
        Location locationForDamagedItems,
        User movedBy,
        SetInspection setInspection) {

    StockMovement stockMovement = new StockMovement();
    stockMovement.setMovementType(StockMovementType.TRANSFER);
    stockMovement.setFromLocation(inspectedSet.getLocation());
    stockMovement.setToLocation(locationForDamagedItems);
    stockMovement.setItem(discrepancyLine.getItem());
    stockMovement.setLot(discrepancyLine.getLot());
    stockMovement.setQty(discrepancyLine.getQty());
    stockMovement.setPostedBy(movedBy);
    stockMovement.setPostedAt(LocalDateTime.now());
    stockMovement.setRefType(StockMovementRefType.SET_INSPECTION);
    stockMovement.setRefId(setInspection.getId());
    stockMovement.setStatus(StockMovementStatus.CLOSED);

    if (comment != null && !comment.trim().isEmpty()) {
      stockMovement.setComment(comment);
    }

    return stockMovement;
  }

  /**
   * Moves items from a set to replenish the set based on the provided set tag ID.
   * This method finds allocated stock movements for the set inspection and updates
   * the set instance materials accordingly. It then updates the status
   *  of the stock movements to "CLOSED".
   *
   * @param setTagId the tag ID of the set for which to move items for replenishment
   * @param userEmail the email of the user performing the operation
   * @return a list of StockMovementDto representing the stock movements that were processed
   * @throws ResourceNotFoundException if the set instance or user is not found
   * @throws IllegalArgumentException if no set inspections or allocated stock
   *      movements are found for the set
   */
  @Transactional
  public List<StockMovementDto> moveItemsToReplenishSetByTagId(String setTagId, String userEmail) {
    List<StockMovementDto> stockMovementDtoList = new ArrayList<>();

    User user = userRepository.findByEmailAndActiveTrue(userEmail)
        .orElseThrow(() -> new UnauthorizedException("User with email " + userEmail
        + " not found or inactive."));

    SetInstance setInstance = setInstanceRepository.findByTagId(setTagId)
        .orElseThrow(() -> new ResourceNotFoundException(
        "Set with tag ID " + setTagId + " not found."));

    List<SetInspection> setInspections = setInspectionRepository
        .findBySetReceiptSetInstanceId(setInstance.getId());

    if (setInspections.isEmpty()) {
      throw new IllegalArgumentException(
        "No set inspections found for set: " + setTagId);
    }

    List<Long> setInspectionIds = setInspections.stream()
        .map(SetInspection::getId)
        .toList();

    List<StockMovement> allocatedStockMovements = stockMovementRepository
        .findByRefTypeAndRefIdInAndStatus(
        StockMovementRefType.SET_INSPECTION,
        setInspectionIds,
        StockMovementStatus.ALLOCATED);

    if (allocatedStockMovements.isEmpty()) {
      throw new IllegalArgumentException(
        "No available replenishments for set: " + setTagId);
    }

    for (StockMovement stockMovement : allocatedStockMovements) {
      Optional<SetInstanceMaterial> setInstanceMaterialOptional = setInstanceMaterialRepository
          .findBySetInstanceIdAndItemIdAndLotId(
          setInstance.getId(),
          stockMovement.getItem().getId(),
          stockMovement.getLot().getId()
        );

      updateSetInstanceMaterialByStockMovement(
          stockMovement, setInstanceMaterialOptional, setInstance
      );

      StockMovement savedStockMovement = updateStockMovementStatus(stockMovement, user);
      StockMovementDto stockMovementDto = StockMovementDtoMapper.mapToDto(savedStockMovement);
      stockMovementDto.setMovedByEmail(userEmail);
      stockMovementDtoList.add(stockMovementDto);
    }
    return stockMovementDtoList;
  }

  private StockMovement updateStockMovementStatus(StockMovement stockMovement, User user) {
    stockMovement.setStatus(StockMovementStatus.CLOSED);
    stockMovement.setUpdatedAt(LocalDateTime.now());
    stockMovement.setUpdatedBy(user);
    return stockMovementRepository.save(stockMovement);
  }

  private void updateSetInstanceMaterialByStockMovement(StockMovement stockMovement,
            Optional<SetInstanceMaterial> setInstanceMaterialOptional, SetInstance inspectedSet) {
    if (setInstanceMaterialOptional.isPresent()) {
      SetInstanceMaterial setInstanceMaterial = setInstanceMaterialOptional.get();
      setInstanceMaterial.setPresentQty(
          setInstanceMaterial.getPresentQty() + stockMovement.getQty());
      setInstanceMaterialRepository.save(setInstanceMaterial);
    } else {
      SetInstanceMaterial newSetInstanceMaterial = new SetInstanceMaterial();
      newSetInstanceMaterial.setItem(stockMovement.getItem());
      newSetInstanceMaterial.setPresentQty(stockMovement.getQty());
      newSetInstanceMaterial.setLot(stockMovement.getLot());
      newSetInstanceMaterial.setSetInstance(inspectedSet);
      setInstanceMaterialRepository.save(newSetInstanceMaterial);
    }
  }

  /**
   * Retrieves details of items that need to be replenished for a set after inspection.
   * This method finds allocated stock movements for the set inspection and returns
   * their details as a list of StockMovementDto.
   *
   * @param inspectionNumber the inspection number of the set for which to retrieve
   *                        replenishment details
   * @return a list of StockMovementDto representing the details of items that need to be
   *      replenished for the set after inspection
   * @throws ResourceNotFoundException if the set inspection is not found
   */
  public List<StockMovementDto> getItemsDetailsForSetReplenishmentAfterInspection(
      String inspectionNumber) {
    SetInspection setInspection = setInspectionRepository.findBySetInspectionNumber(
        inspectionNumber).orElseThrow(() -> new ResourceNotFoundException(
          "Set inspection with number " + inspectionNumber + " not found."));
    List<StockMovement> stockMovementListAfterInspection = stockMovementRepository
        .findDetailedByRefTypeAndRefIdAndStatus(StockMovementRefType.SET_INSPECTION,
          setInspection.getId(), StockMovementStatus.ALLOCATED);
    return stockMovementListAfterInspection.stream()
      .map(StockMovementDtoMapper::mapToDto)
      .map(dto -> {
        dto.setMovedByEmail(null);
        return dto;
      })
      .toList();

  }


  private StockMovement createStockMovementFromSetReceipt(String comment,
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
    return stockMovementRepository.save(stockMovement);
  }

}

