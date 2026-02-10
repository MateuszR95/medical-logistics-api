package pl.mateusz.medicallogistics.medicallogisticsapi.set.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetBase;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.repository.SetBaseRepository;

/**
 * Service for managing set bases in the medical logistics system.
 * This service is responsible for handling business logic related to set bases,
 * such as creating, updating, and retrieving set base information.
 */
@Service
public class SetBaseService {

  private final SetBaseRepository setBaseRepository;

  /**
   * Constructs a SetBaseService with the specified repository.
   *
   * @param setBaseRepository the repository for managing SetBase entities
   */
  public SetBaseService(SetBaseRepository setBaseRepository) {
    this.setBaseRepository = setBaseRepository;
  }

  /**
      * Finds a SetBase entity by its catalog number.
      *
      * @param catalogNumber the catalog number to search for
      * @return an Optional containing the found SetBase entity, or empty if not found
      */
  public Optional<SetBase> findByCatalogNumber(String catalogNumber) {
    return setBaseRepository.findByCatalogNumber(catalogNumber);
  }
}
