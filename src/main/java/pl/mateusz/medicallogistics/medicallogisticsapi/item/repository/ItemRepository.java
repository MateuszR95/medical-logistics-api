package pl.mateusz.medicallogistics.medicallogisticsapi.item.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.domain.Item;

/**
 * Repository interface for managing Item entities.
 */
public interface ItemRepository extends JpaRepository<Item, Long> {

  /**
   * Checks if an item with the given reference number exists in the database.
   *
   * @param refNumber the reference number to check for existence
   * @return true if an item with the given reference number exists, false otherwise
   */
  boolean existsByRefNumber(String refNumber);

  /**
   * Retrieves an item by its reference number.
   *
   * @param refNumber the reference number of the item to retrieve
   * @return an Optional containing the found Item, or empty if no
   *      item with the given reference number exists
   */
  Optional<Item> findByRefNumber(String refNumber);
}
