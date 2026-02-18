package pl.mateusz.medicallogistics.medicallogisticsapi.set.returns.repository;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.returns.domain.SetReceipt;


/**
 * Repository interface for managing SetReceipt entities.
 * Extends JpaRepository to provide CRUD operations and database interaction
 * for the SetReceipt entity.
 */
public interface SetReceiptRepository extends JpaRepository<SetReceipt, Long> {

  /**
      * Counts the number of SetReceipt entities that have a receivedAt date
      *    matching the specified date.
      *
      * @param receivedAt the date to match against the receivedAt field of SetReceipt entities
      * @return the count of SetReceipt entities with the specified receivedAt date
      */
  Long countAllByReceivedAt(LocalDate receivedAt);

  /**
   * Finds the most recent SetReceipt for a given set instance ID, ordered by
   * the receivedAt timestamp in descending order.
   *
   * @param setInstanceId the ID of the set instance to search for
   * @return an Optional containing the most recent SetReceipt if found,
   *      or an empty Optional if no matching SetReceipt exists
   */
  Optional<SetReceipt> findTopBySetInstanceIdOrderByReceivedAtDesc(Long setInstanceId);
}
