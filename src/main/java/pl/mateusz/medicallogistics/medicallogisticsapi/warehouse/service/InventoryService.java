package pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mateusz.medicallogistics.medicallogisticsapi.config.InboundConfiguration;
import pl.mateusz.medicallogistics.medicallogisticsapi.consignment.items.request.domain.ConsignmentItemRequest;
import pl.mateusz.medicallogistics.medicallogisticsapi.consignment.items.request.domain.ConsignmentItemRequestLine;
import pl.mateusz.medicallogistics.medicallogisticsapi.exception.ResourceNotFoundException;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.domain.InboundReceiptLine;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.domain.Item;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.repository.ItemRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.lot.domain.Lot;
import pl.mateusz.medicallogistics.medicallogisticsapi.lot.repository.LotRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetInstance;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.domain.SetInspection;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.repository.SetInspectionRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.StockMovementRefType;
import pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.StockMovementStatus;
import pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.StockMovementType;
import pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.domain.StockMovement;
import pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.repository.StockMovementRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.user.domain.User;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.LocationZone;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.domain.Inventory;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.domain.Location;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.repository.InventoryRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.repository.LocationRepository;


/**
 * Service for managing inventory in the warehouse.
 * This service is responsible for handling business logic related to inventory management,
 * such as tracking stock levels, updating inventory records, and managing inventory movements.
 */
@Service
public class InventoryService {

  private final InventoryRepository inventoryRepository;
  private final InboundConfiguration inboundConfiguration;

  private final LocationRepository locationRepository;

  private final ItemRepository itemRepository;

  private final LotRepository lotRepository;

  private final SetInspectionRepository setInspectionRepository;

  private final StockMovementRepository stockMovementRepository;
  private static final String ITEM_TYPE_LINE = "ITEM";

  private static final String LOCATION_CODE_FOR_DAMAGED_PARTS = "QUAR-IN";


  /**
   * Constructs an InventoryService with the specified dependencies.
   *
   * @param inventoryRepository the repository for managing Inventory entities
   * @param inboundConfiguration the configuration containing settings for inbound processing
   * @param locationRepository the repository for managing Location entities
   * @param itemRepository the repository for managing Item entities
   * @param lotRepository the repository for managing Lot entities
   *
   */
  public InventoryService(InventoryRepository inventoryRepository,
                          InboundConfiguration inboundConfiguration,
                          LocationRepository locationRepository,
                          ItemRepository itemRepository,
                          LotRepository lotRepository,
                          SetInspectionRepository setInspectionRepository,
                          StockMovementRepository stockMovementRepository) {
    this.inventoryRepository = inventoryRepository;
    this.inboundConfiguration = inboundConfiguration;
    this.locationRepository = locationRepository;
    this.itemRepository = itemRepository;
    this.lotRepository = lotRepository;
    this.setInspectionRepository = setInspectionRepository;
    this.stockMovementRepository = stockMovementRepository;
  }

  /**
   * Updates the inventory based on the provided inbound receipt line.
   * If the line type is "ITEM", it will either update an existing inventory
   * record or create a new one.
   *
   * @param inboundReceiptLine the line item from the inbound receipt to process
   *                          for inventory update
   * @throws RuntimeException if the default receipt location, item, or lot is not found
   */
  @Transactional
  public void updateInventoryAfterInboundReceipt(InboundReceiptLine inboundReceiptLine) {

    if (!ITEM_TYPE_LINE.equals(inboundReceiptLine.getLineType().name())) {
      return;
    }
    long qty = inboundReceiptLine.getQty();
    String ref = inboundReceiptLine.getItemRefNumber();
    String lotNo = inboundReceiptLine.getLotNumber();

    Location location = locationRepository.findByCode(
        inboundConfiguration.getDefaultReceiptLocationCode()
    ).orElseThrow(() -> new RuntimeException("Default receipt location not found"));

    inventoryRepository.findByLocationAndItemRefNumberAndLotLotNumber(location, ref, lotNo)
        .ifPresentOrElse(inventory -> {
          inventory.setQty(inventory.getQty() + qty);
          inventoryRepository.save(inventory);
        }, () -> {
          Item item = itemRepository.findByRefNumber(ref)
              .orElseThrow(() -> new RuntimeException("Item not found with ref number: " + ref));
          Lot lot = lotRepository.findByLotNumber(lotNo)
              .orElseThrow(() -> new RuntimeException("Lot not found with lot number: " + lotNo));
          Inventory newInventory = new Inventory();
          newInventory.setLocation(location);
          newInventory.setItem(item);
          newInventory.setLot(lot);
          newInventory.setQty(qty);
          inventoryRepository.save(newInventory);
        });
  }

  /**
   * Adds overage parts identified during a set inspection to the inventory.
   * If an inventory record already exists for the specified location, item, and lot,
   * the quantity is updated. Otherwise, a new inventory record is created.
   *
   * @param stockMovement the stock movement containing details about the location,
   *                      item, lot, and quantity of overage parts to be added
   */
  @Transactional
  public void addOveragePartsAfterSetInspectionToInventory(StockMovement stockMovement) {
    inventoryRepository.findByLocationAndItemRefNumberAndLotLotNumber(
        stockMovement.getToLocation(), stockMovement.getItem().getRefNumber(),
        stockMovement.getLot().getLotNumber())
        .ifPresentOrElse(inventory -> {
          inventory.setQty(inventory.getQty() + stockMovement.getQty());
          inventoryRepository.save(inventory);
        }, () -> {
          Inventory newInventory = new Inventory();
          newInventory.setLocation(stockMovement.getToLocation());
          newInventory.setItem(stockMovement.getItem());
          newInventory.setLot(stockMovement.getLot());
          newInventory.setQty(stockMovement.getQty());
          inventoryRepository.save(newInventory);
        });
  }

  /**
   * Updates the inventory and creates stock movements for replenishing a set inspection
   * based on the provided consignment item request. This method allocates inventory
   * for the items in the request and creates corresponding stock movements to transfer
   * the allocated items to the set instance location.
   *
   * @param request the consignment item request containing details about the items to be allocated
   *                and the source reference for the set inspection
   * @return a list of StockMovement entities representing the initiated
   *      stock movements for replenishment
   * @throws ResourceNotFoundException if the set inspection with the specified
   *      source reference is not found
   * @throws IllegalStateException if there is insufficient inventory to fulfill the request
   */
  @Transactional
  public List<StockMovement> updateInventoryAndCreateStockMovementAfterSetInspection(
      ConsignmentItemRequest request) {

    List<ConsignmentItemRequestLine> lines = request.getLines().stream()
        .filter(line -> line.getAllocatedQty() > 0)
        .toList();
    List<StockMovement> initiatedStockMovements = new ArrayList<>();
    SetInspection setInspection = setInspectionRepository.findBySetInspectionNumber(
        request.getSourceRef()).orElseThrow(()
            -> new ResourceNotFoundException("Set inspection with number "
        + request.getSourceRef() + " not found."));
    for (ConsignmentItemRequestLine line : lines) {
      boolean sterile = line.getItem().isSterile();
      String refNumber = line.getItem().getRefNumber();
      long remaining = line.getAllocatedQty();

      List<Inventory> inventoryList = sterile
          ? inventoryRepository.findByItemRefNumberAndLocationZoneOrderByLotExpirationDateAsc(
            refNumber, LocationZone.STORAGE)
          : inventoryRepository.findByItemRefNumberAndLocationZoneOrderByIdAsc(refNumber,
          LocationZone.STORAGE);

      for (Inventory inventory : inventoryList) {
        if (remaining == 0) {
          break;
        }
        long available = inventory.getQty();
        if (available <= 0) {
          continue;
        }
        long partToTake = Math.min(available, remaining);
        inventory.setQty(available - partToTake);
        inventory.setAllocatedQty(inventory.getAllocatedQty() + partToTake);
        Inventory updatedInventory = inventoryRepository.save(inventory);
        StockMovement stockMovementToReplenishSet =
            createReplenishmentStockMovementForSetInspection(updatedInventory,
            request.getCreatedBy(), setInspection, request.getSetInstance(),
            partToTake);
        initiatedStockMovements.add(stockMovementToReplenishSet);
        remaining -= partToTake;
      }
      if (remaining > 0) {
        throw new IllegalStateException(
          "Insufficient inventory for item " + refNumber + ". Missing quantity: " + remaining);
      }
    }
    return initiatedStockMovements;
  }

  /**
   * Updates the inventory for damaged parts identified during a set inspection. This method
   * adds the quantity of damaged parts to a designated location for damaged items. If an
   * inventory record already exists for the specified location, item, and lot, the quantity
   * is updated. Otherwise, a new inventory record is created.
   *
   * @param stockMovement the stock movement containing details about the item, lot, and quantity
   *                      of damaged parts to be added to the inventory
   * @throws IllegalArgumentException if the stock movement is null or does not contain
   *      necessary details
   * @throws ResourceNotFoundException if the location for damaged parts is not found
   */
  @Transactional
  public void updateInventoryForDamagedPartsAfterInspection(StockMovement stockMovement) {
    if (stockMovement == null) {
      throw new IllegalArgumentException("Stock movement cannot be null.");
    }
    if (stockMovement.getItem() == null || stockMovement.getLot() == null) {
      throw new IllegalArgumentException("Stock movement must contain item and lot.");
    }
    if (stockMovement.getQty() <= 0) {
      return;
    }
    Location damagedPartsLocation = locationRepository.findByCode(LOCATION_CODE_FOR_DAMAGED_PARTS)
        .orElseThrow(() -> new ResourceNotFoundException(
        "Location with code: " + LOCATION_CODE_FOR_DAMAGED_PARTS + " not found"));
    Optional<Inventory> inventoryOptional = inventoryRepository
        .findByLocationAndItemRefNumberAndLotLotNumber(
        damagedPartsLocation,
        stockMovement.getItem().getRefNumber(),
        stockMovement.getLot().getLotNumber());

    if (inventoryOptional.isPresent()) {
      Inventory inventory = inventoryOptional.get();
      inventory.setQty(inventory.getQty() + stockMovement.getQty());
      inventoryRepository.save(inventory);
    } else {
      Inventory newInventory = new Inventory();
      newInventory.setLocation(damagedPartsLocation);
      newInventory.setItem(stockMovement.getItem());
      newInventory.setLot(stockMovement.getLot());
      newInventory.setQty(stockMovement.getQty());
      inventoryRepository.save(newInventory);
    }
  }

  private StockMovement createReplenishmentStockMovementForSetInspection(Inventory inventory,
                                                           User user, SetInspection setInspection,
                                                           SetInstance setInstance,
                                                           long allocatedItemsInInventoryLine) {
    StockMovement stockMovement = new StockMovement();
    stockMovement.setMovementType(StockMovementType.TRANSFER);
    stockMovement.setFromLocation(inventory.getLocation());
    stockMovement.setToLocation(setInstance.getLocation());
    stockMovement.setItem(inventory.getItem());
    stockMovement.setLot(inventory.getLot());
    stockMovement.setQty(allocatedItemsInInventoryLine);
    stockMovement.setPostedBy(user);
    stockMovement.setPostedAt(LocalDateTime.now());
    stockMovement.setRefType(StockMovementRefType.SET_INSPECTION);
    stockMovement.setRefId(setInspection.getId());
    stockMovement.setStatus(StockMovementStatus.ALLOCATED);
    return stockMovementRepository.save(stockMovement);
  }

}


