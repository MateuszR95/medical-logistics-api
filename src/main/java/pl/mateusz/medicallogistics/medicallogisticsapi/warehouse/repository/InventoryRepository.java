package pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.domain.Inventory;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.domain.Location;


/**
 * Repository interface for managing Inventory entities.
 * Extends JpaRepository to provide CRUD operations and database interaction
 * for the Inventory entity.
 */
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

  /**
   * Finds a list of Inventory entities by their associated Location.
   *
   * @param location the Location entity to search for
   * @return a List of Inventory entities associated with the given Location
   */
  List<Inventory> findByLocation(Location location);

  /**
   * Finds an Inventory entity by its associated Location, item reference number, and lot number.
   *
   * @param location the Location entity to search for
   * @param itemRefNumber the reference number of the item to search for
   * @param lotNumber the lot number of the item to search for
   * @return an Optional containing the found Inventory entity, or empty if
   *      no matching Inventory is found
   */
  Optional<Inventory> findByLocationAndItemRefNumberAndLotLotNumber(
      Location location, String itemRefNumber, String lotNumber);

}
