package pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.StockMovementRefType;
import pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.StockMovementStatus;
import pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.domain.StockMovement;


/**
 * Repository interface for managing StockMovement entities.
 * Extends JpaRepository to provide CRUD operations and database interaction
 * for the StockMovement entity.
 */
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

  /**
   * Finds a list of StockMovement entities by their reference type, reference ID, and status.
   *
   * @param refType the reference type to search for
   * @param refId the reference ID to search for
   * @param status the status to search for
   * @return a List of StockMovement entities matching the given criteria
   */
  @Query("select sm from StockMovement sm join fetch sm.item left join fetch sm.lot "
      + "where sm.refType = :refType and sm.refId = :refId "
      + "and sm.status = :status")
  List<StockMovement> findDetailedByRefTypeAndRefIdAndStatus(StockMovementRefType refType,
                          Long refId, StockMovementStatus status);

  /**
   * Finds a list of StockMovement entities by their reference type,
   * a list of reference IDs, and status.
   *
   * @param refType the reference type to search for
   * @param refIds the list of reference IDs to search for
   * @param status the status to search for
   * @return a List of StockMovement entities matching the given criteria
   */
  List<StockMovement> findByRefTypeAndRefIdInAndStatus(StockMovementRefType refType,
                         List<Long> refIds, StockMovementStatus status);



}
