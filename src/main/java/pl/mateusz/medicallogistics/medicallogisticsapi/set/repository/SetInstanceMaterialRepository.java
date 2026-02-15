package pl.mateusz.medicallogistics.medicallogisticsapi.set.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetInstanceMaterial;

/**
 * Repository interface for managing SetInstanceMaterial entities.
 * Extends JpaRepository to provide CRUD operations and database interaction
 * for the SetInstanceMaterial entity.
 */
public interface SetInstanceMaterialRepository extends JpaRepository<SetInstanceMaterial, Long> {

  /**
   * Finds a SetInstanceMaterial by the IDs of its associated SetInstance, Item, and Lot.
   *
   * @param setInstanceId the ID of the associated SetInstance
   * @param itemId the ID of the associated Item
   * @param lotId the ID of the associated Lot
   * @return an Optional containing the found SetInstanceMaterial, or empty if not found
   */
  Optional<SetInstanceMaterial> findBySetInstanceIdAndItemIdAndLotId(
      Long setInstanceId, Long itemId, Long lotId
  );
}
