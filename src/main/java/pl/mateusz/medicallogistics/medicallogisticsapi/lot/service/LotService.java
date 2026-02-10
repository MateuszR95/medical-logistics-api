package pl.mateusz.medicallogistics.medicallogisticsapi.lot.service;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.domain.Item;
import pl.mateusz.medicallogistics.medicallogisticsapi.lot.domain.Lot;
import pl.mateusz.medicallogistics.medicallogisticsapi.lot.repository.LotRepository;


/**
 * Service for managing lots in the medical logistics system.
 * This service is responsible for handling business logic related to lots,
 * such as creating, updating, and retrieving lot information.
 */
@Service
public class LotService {

  private final LotRepository lotRepository;

  /**
   * Constructs a LotService with the specified repository.
   *
   * @param lotRepository the repository for managing Lot entities
   */
  public LotService(LotRepository lotRepository) {
    this.lotRepository = lotRepository;
  }

  /**
   * Retrieves a lot by its lot number.
   *
   * @param lotNumber the lot number to search for
   * @return an Optional containing the Lot if found, or empty if not found
   */
  public Optional<Lot> getLotByLotNumber(String lotNumber) {
    return lotRepository.findByLotNumber(lotNumber);
  }

  /**
   * Creates and saves a new lot with the specified details.
   *
   * @param lotNumber the unique lot number for the new lot
   * @param item the associated Item for the lot
   * @param expirationDate the expiration date of the lot
   */
  @Transactional
  public Lot createAndSaveLot(String lotNumber, Item item, LocalDate expirationDate) {
    Lot lot = new Lot();
    lot.setLotNumber(lotNumber);
    lot.setItem(item);
    lot.setExpirationDate(expirationDate);
    return lotRepository.save(lot);
  }
}
