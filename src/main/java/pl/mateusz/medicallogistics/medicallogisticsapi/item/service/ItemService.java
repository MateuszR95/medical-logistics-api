package pl.mateusz.medicallogistics.medicallogisticsapi.item.service;

import org.springframework.stereotype.Service;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.domain.Item;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.repository.ItemRepository;

/**
 * Service class for managing medical items.
 */
@Service
public class ItemService {

  private final ItemRepository itemRepository;

  /**
   * Constructs an ItemService with the specified ItemRepository.
   *
   * @param itemRepository the repository for managing Item entities
   */
  public ItemService(ItemRepository itemRepository) {
    this.itemRepository = itemRepository;
  }

  /**
   * Retrieves an Item by its reference number.
   *
   * @param refNumber the reference number of the item to retrieve
   * @return the Item corresponding to the specified reference number
   * @throws IllegalStateException if no item is found for the given reference number
   */
  public Item getItemByRefNumber(String refNumber) {
    return itemRepository.findByRefNumber(refNumber)
        .orElseThrow(() -> new IllegalStateException("Item not found for ref number: "
          + refNumber));
  }
}
