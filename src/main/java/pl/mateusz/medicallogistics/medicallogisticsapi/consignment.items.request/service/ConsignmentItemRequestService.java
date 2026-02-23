package pl.mateusz.medicallogistics.medicallogisticsapi.consignment.items.request.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mateusz.medicallogistics.medicallogisticsapi.consignment.items.request.ConsignmentItemRequestStatus;
import pl.mateusz.medicallogistics.medicallogisticsapi.consignment.items.request.ConsignmentItemRequestType;
import pl.mateusz.medicallogistics.medicallogisticsapi.consignment.items.request.domain.ConsignmentItemRequest;
import pl.mateusz.medicallogistics.medicallogisticsapi.consignment.items.request.domain.ConsignmentItemRequestLine;
import pl.mateusz.medicallogistics.medicallogisticsapi.consignment.items.request.repository.ConsignmentItemRequestLineRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.consignment.items.request.repository.ConsignmentItemRequestRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.exception.ResourceNotFoundException;
import pl.mateusz.medicallogistics.medicallogisticsapi.exception.UnauthorizedException;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.domain.Item;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.repository.ItemRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetInstance;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.domain.SetInspection;
import pl.mateusz.medicallogistics.medicallogisticsapi.user.domain.User;
import pl.mateusz.medicallogistics.medicallogisticsapi.user.repository.UserRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.LocationZone;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.domain.Inventory;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.repository.InventoryRepository;


/**
 * Service class for managing ConsignmentItemRequest entities.
 * Provides business logic and operations related to ConsignmentItemRequest.
 */
@Service
public class ConsignmentItemRequestService {

  private final ConsignmentItemRequestRepository consignmentItemRequestRepository;
  private final UserRepository userRepository;
  private final ItemRepository itemRepository;

  private final InventoryRepository inventoryRepository;

  private final ConsignmentItemRequestLineRepository consignmentItemRequestLineRepository;

  /**
   * Constructs a new instance of the ConsignmentItemRequestService.
   *
   * @param consignmentItemRequestRepository the repository for managing
   *                                        ConsignmentItemRequest entities
   * @param userRepository the repository for managing User entities
   * @param itemRepository the repository for managing Item entities
   * @param inventoryRepository the repository for managing Inventory entities
   * @param consignmentItemRequestLineRepository the repository for managing
   *                                            ConsignmentItemRequestLine entities
   */
  public ConsignmentItemRequestService(ConsignmentItemRequestRepository
                                         consignmentItemRequestRepository,
                                       UserRepository userRepository, ItemRepository itemRepository,
                                       InventoryRepository inventoryRepository,
                                       ConsignmentItemRequestLineRepository
                                         consignmentItemRequestLineRepository) {
    this.consignmentItemRequestRepository = consignmentItemRequestRepository;
    this.userRepository = userRepository;
    this.itemRepository = itemRepository;
    this.inventoryRepository = inventoryRepository;
    this.consignmentItemRequestLineRepository = consignmentItemRequestLineRepository;
  }

  /**
   * Initiates a replenishment process for a specific set instance based on missing parts identified
   * during a set inspection. Creates a new consignment item request and its
   * associated request lines.
   *
   * @param userEmail the email of the user initiating the replenishment
   * @param setInstance the set instance for which the replenishment is being initiated
   * @param setInspection the set inspection that identified the missing parts
   * @param missingPartsByRefNumber a map of item reference numbers to the quantities
   *                               of missing parts
   * @throws UnauthorizedException if the user with the provided email is not found or inactive
   * @throws ResourceNotFoundException if an item with the specified reference number is not found
   */
  @Transactional
  public void initiateReplenishmentToSetInstance(
      String userEmail,
      SetInstance setInstance,
      SetInspection setInspection,
      Map<String, Long> missingPartsByRefNumber) {
    ConsignmentItemRequest request = new ConsignmentItemRequest();
    request.setRequestNumber(generateConsignmentItemRequestNumber());
    request.setType(ConsignmentItemRequestType.SET_REPLENISHMENT);
    request.setStatus(ConsignmentItemRequestStatus.OPEN);

    User user = userRepository.findByEmailAndActiveTrue(userEmail).orElseThrow(() ->
      new UnauthorizedException("User not found or inactive: " + userEmail));

    request.setCreatedBy(user);
    request.setCreatedAt(LocalDateTime.now());
    request.setSetInstance(setInstance);
    request.setPlannedShippingDate(LocalDate.now());
    request.setCustomer(null);
    request.setSourceRef("SET_INSPECTION:" + setInspection.getSetInspectionNumber());

    ConsignmentItemRequest saved = consignmentItemRequestRepository.save(request);

    List<ConsignmentItemRequestLine> requestLines = new ArrayList<>();

    for (var entry : missingPartsByRefNumber.entrySet()) {
      String refNumber = entry.getKey();
      long requested = entry.getValue() == null ? 0L : entry.getValue();
      if (requested <= 0) {
        continue;
      }
      Item item = itemRepository.findByRefNumber(refNumber).orElseThrow(() ->
        new ResourceNotFoundException("Item not found with ref number: " + refNumber));

      List<Inventory> storage = inventoryRepository
          .findByItemRefNumberAndLocationZone(refNumber, LocationZone.STORAGE);

      long inStock = storage.stream().mapToLong(Inventory::getQty).sum();
      long allocated = Math.min(requested, inStock);
      long backordered = requested - allocated;
      ConsignmentItemRequestLine line = new ConsignmentItemRequestLine();
      line.setRequest(saved);
      line.setItem(item);
      line.setRequestedQty(requested);
      line.setAllocatedQty(allocated);
      line.setBackorderedQty(backordered);
      line.setShippedQty(0);
      requestLines.add(consignmentItemRequestLineRepository.save(line));
    }
    saved.setLines(requestLines);
    consignmentItemRequestRepository.save(saved);
  }

  private String generateConsignmentItemRequestNumber() {
    return "CN-RQ-" + LocalDate.now() + "-"
      + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
  }
}
