package pl.mateusz.medicallogistics.medicallogisticsapi.set.receipt.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mateusz.medicallogistics.medicallogisticsapi.exception.BadRequestException;
import pl.mateusz.medicallogistics.medicallogisticsapi.exception.ResourceNotFoundException;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.domain.InboundReceiptBatch;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.SetStatus;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetInstance;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.receipt.domain.SetReceipt;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.receipt.dto.SetReceiptDto;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.receipt.dto.SetReceiptDtoMapper;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.receipt.repository.SetReceiptRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.repository.SetInstanceRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.user.domain.User;
import pl.mateusz.medicallogistics.medicallogisticsapi.user.repository.UserRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.domain.Location;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.repository.LocationRepository;

/**
 * Service class for managing set receipts, which represent the return of medical sets.
 * This service provides methods to handle the business logic related to set receipts,
 * such as creating new receipts, retrieving existing receipts, and updating receipt information.
 */
@Service
public class SetReceiptService {

  private final SetReceiptRepository setReceiptRepository;

  private final SetInstanceRepository setInstanceRepository;

  private final UserRepository userRepository;

  private final LocationRepository locationRepository;


  /**
   * Constructs a new instance of the SetReceiptService.
   *
   * @param setReceiptRepository the repository for managing SetReceipt entities
   * @param setInstanceRepository the repository for managing SetInstance entities
   * @param userRepository the repository for managing User entities
   * @param locationRepository the repository for managing Location entities
   */
  public SetReceiptService(SetReceiptRepository setReceiptRepository,
                           SetInstanceRepository setInstanceRepository,
                           UserRepository userRepository,
                           LocationRepository locationRepository) {
    this.setReceiptRepository = setReceiptRepository;
    this.setInstanceRepository = setInstanceRepository;
    this.userRepository = userRepository;
    this.locationRepository = locationRepository;
  }

  /**
   * Creates and saves a new SetReceipt based on the provided SetInstance and InboundReceiptBatch.
   * This method generates a unique receipt number, sets the received date and
   * user based on the inbound batch,
   * and saves the new SetReceipt to the database.
   *
   * @param setInstance The SetInstance associated with the receipt.
   * @param inboundBatch The InboundReceiptBatch containing information about the receipt.
   */
  @Transactional
  public void createAndSaveSetReceiptFromInboundReceipt(SetInstance setInstance,
                                            InboundReceiptBatch inboundBatch) {
    SetReceipt setReceipt = new SetReceipt();
    setReceipt.setReceiptNumber(generateReceiptNumber());
    setReceipt.setSetInstance(setInstance);
    setReceipt.setReceivedAt(inboundBatch.getCreatedAt());
    setReceipt.setReceivedBy(inboundBatch.getCreatedBy());
    setReceipt.setComment("Set receipt created from inbound receipt batch: "
        + inboundBatch.getBatchNumber());
    setReceiptRepository.save(setReceipt);
  }

  /**
   * Initiates the creation of a new SetReceipt based on the provided set tag ID, user email,
   *    and an optional comment.
   * This method retrieves the associated SetInstance and User, generates a unique receipt number,
   * sets the received date and user, and saves the new SetReceipt to the database.
   *
   * @param setTagId The tag ID of the SetInstance for which the receipt is being created.
   * @param userEmail The email of the user receiving the set.
   * @param comment An optional comment to be associated with the receipt.
   * @return A SetReceiptDto containing details of the created receipt.
   * @throws ResourceNotFoundException if the SetInstance or User cannot be
   *      found based on the provided parameters.
   */
  @Transactional
  public SetReceiptDto initiateSetReceiptFromCustomer(String setTagId, String locationCode,
                                                      String comment, String userEmail) {
    SetInstance setInstance = setInstanceRepository.findByTagId(setTagId)
        .orElseThrow(() -> new ResourceNotFoundException("Set instance with tag ID "
          + setTagId + " not found"));
    if (setInstance.getCustomer() == null) {
      throw new BadRequestException("Set instance with tag ID " + setTagId
        + " is not associated with any customer");
    }
    User user = userRepository.findByEmailAndActiveTrue(userEmail)
        .orElseThrow(() -> new ResourceNotFoundException("Active user with email "
          + userEmail + " not found"));
    SetReceipt setReceipt = new SetReceipt();
    setReceipt.setReceiptNumber(generateReceiptNumber());
    setReceipt.setSetInstance(setInstance);
    setReceipt.setReceivedAt(LocalDateTime.now());
    setReceipt.setReceivedBy(user);
    if (comment != null && !comment.trim().isEmpty()) {
      setReceipt.setComment(comment.trim());
    }
    SetReceipt saved = setReceiptRepository.save(setReceipt);
    updateSetInstanceAfterReceipt(locationCode, setInstance);
    return SetReceiptDtoMapper.mapToDto(saved);
  }


  private void updateSetInstanceAfterReceipt(String locationCode, SetInstance setInstance) {
    Location location = locationRepository.findByCode(locationCode).orElseThrow(
        () -> new ResourceNotFoundException("Location with code " + locationCode + " not found"));
    setInstance.setLocation(location);
    setInstance.setSetStatus(SetStatus.RETURNED_PENDING_CHECK);
    setInstance.setCustomer(null);
    setInstance.setShippedDate(null);
    setInstance.setSurgeryDate(null);
    setInstance.setExpectedReturnDate(null);
    setInstance.setReturnedDate(LocalDate.now());
    setInstanceRepository.save(setInstance);
  }

  private String generateReceiptNumber() {
    return "SET-RECEIPT-" + LocalDate.now() + "-"
      + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
  }
}
