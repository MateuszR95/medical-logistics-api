package pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.LocationZone;
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

  /**
   * Finds a list of Inventory entities by their associated item reference number
   * and location zone.
   *
   * @param refNumber the reference number of the item to search for
   * @param locationZone the zone of the location to search within
   * @return a List of Inventory entities matching the given item reference number
   *         and location zone
   */
  List<Inventory> findByItemRefNumberAndLocationZone(String refNumber, LocationZone locationZone);

  /**
   * Finds a list of Inventory entities by their associated item reference number
   * and location zone, ordered by lot expiration date in ascending order.
   *
   * @param refNumber the reference number of the item to search for
   * @param locationZone the zone of the location to search within
   * @return a List of Inventory entities matching the given item reference number
   *         and location zone, ordered by lot expiration date in ascending order
   */
  List<Inventory> findByItemRefNumberAndLocationZoneOrderByLotExpirationDateAsc(
      String refNumber, LocationZone locationZone);

  /**
   * Finds a list of Inventory entities by their associated item reference number
   * and location zone, ordered by ID in ascending order.
   *
   * @param itemRefNumber the reference number of the item to search for
   * @param locationZone the zone of the location to search within
   * @return a List of Inventory entities matching the given item reference number
   *         and location zone, ordered by ID in ascending order
   */
  List<Inventory> findByItemRefNumberAndLocationZoneOrderByIdAsc(String itemRefNumber,
                                                                 LocationZone locationZone);

}
