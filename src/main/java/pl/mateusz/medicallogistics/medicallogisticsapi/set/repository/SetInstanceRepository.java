package pl.mateusz.medicallogistics.medicallogisticsapi.set.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.SetStatus;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetInstance;

/**
 * Repository interface for SetInstance entities.
 */
public interface SetInstanceRepository extends JpaRepository<SetInstance, Long> {

  /**
   * Checks if a SetInstance with the given tag ID exists.
   *
   * @param tagId the tag ID to check
   * @return true if a SetInstance with the given tag ID exists, false otherwise
   */
  boolean existsByTagId(String tagId);

  /**
   * Finds a SetInstance by its tag ID.
   *
   * @param setTagId the tag ID of the SetInstance to find
   * @return an Optional containing the found SetInstance, or empty if no
   *      SetInstance with the given tag ID exists
   */
  Optional<SetInstance> findByTagId(String setTagId);

  /**
   * Finds all SetInstances with the given SetStatus.
   *
   * @param setStatus the SetStatus to filter by
   * @return a List of SetInstances with the specified SetStatus
   */
  List<SetInstance> findBySetStatus(SetStatus setStatus);

  /**
   * Finds all SetInstances that are pending checking, which includes those with
   * the status 'PENDING_CHECKING' or 'CHECKING_IN_PROGRESS'.
   *
   * @return a List of SetInstances that are pending checking
   */
  @Query("select si from SetInstance si where si.setStatus = 'RETURNED_PENDING_CHECK' "
      + "or si.setStatus = 'INBOUND'")
  List<SetInstance> findAllSetInstancesPendingChecking();

  /**
   * Finds all SetInstances in the database.
   *
   * @return a List of all SetInstances
   */
  List<SetInstance> findAllBy();
}
