package pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mateusz.medicallogistics.medicallogisticsapi.config.InboundConfiguration;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.domain.InboundReceiptLine;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.domain.Item;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.repository.ItemRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.lot.domain.Lot;
import pl.mateusz.medicallogistics.medicallogisticsapi.lot.repository.LotRepository;
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
  private static final String ITEM_TYPE_LINE = "ITEM";


  /**
   * Constructs an InventoryService with the specified dependencies.
   *
   * @param inventoryRepository the repository for managing Inventory entities
   * @param inboundConfiguration the configuration containing settings for inbound processing
   * @param locationRepository the repository for managing Location entities
   * @param itemRepository the repository for managing Item entities
   * @param lotRepository the repository for managing Lot entities
   */
  public InventoryService(InventoryRepository inventoryRepository,
                          InboundConfiguration inboundConfiguration,
                          LocationRepository locationRepository,
                          ItemRepository itemRepository,
                          LotRepository lotRepository) {
    this.inventoryRepository = inventoryRepository;
    this.inboundConfiguration = inboundConfiguration;
    this.locationRepository = locationRepository;
    this.itemRepository = itemRepository;
    this.lotRepository = lotRepository;
  }

  /**
   * Updates the inventory based on the provided inbound receipt line.
   * If the line type is "ITEM", it will either update an existing inventory
   * record or create a new one.
   * @param inboundReceiptLine the line item from the inbound receipt to process for inventory update
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
}
