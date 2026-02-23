package pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mateusz.medicallogistics.medicallogisticsapi.exception.ResourceNotFoundException;
import pl.mateusz.medicallogistics.medicallogisticsapi.exception.UnauthorizedException;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetBaseMaterial;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetInstance;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetInstanceMaterial;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.SetInspectionStatus;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.dto.SetInspectionDiscrepancyLineDto;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.dto.SetInspectionDiscrepancyListDto;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.service.SetInspectionDiscrepancyLineService;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.domain.SetInspection;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.dto.SetMissingItemDto;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.repository.SetInspectionRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.repository.SetBaseMaterialRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.repository.SetInstanceMaterialRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.repository.SetInstanceRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.returns.domain.SetReceipt;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.returns.repository.SetReceiptRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.user.domain.User;
import pl.mateusz.medicallogistics.medicallogisticsapi.user.repository.UserRepository;

/**
 * Service class for managing set inspections.
 */
@Service
public class SetInspectionService {

  private final SetInspectionRepository setInspectionRepository;
  private final SetInstanceMaterialRepository setInstanceMaterialRepository;
  private final SetBaseMaterialRepository setBaseMaterialRepository;

  private final SetInstanceRepository setInstanceRepository;

  private final UserRepository userRepository;

  private final SetReceiptRepository setReceiptRepository;

  private final SetInspectionDiscrepancyLineService setInspectionDiscrepancyLineService;

  /**
   * Constructs a new SetInspectionService with the specified dependencies.
   *
   * @param setInspectionRepository the repository for managing SetInspection entities
   * @param setInstanceMaterialRepository the repository for managing SetInstanceMaterial entities
   * @param setBaseMaterialRepository the repository for managing SetBaseMaterial entities
   * @param setInstanceRepository the repository for managing SetInstance entities
   * @param userRepository the repository for managing User entities
   * @param setReceiptRepository the repository for managing SetReceipt entities
   * @param setInspectionDiscrepancyLineService the service for managing
   *                                            SetInspectionDiscrepancyLine entities
   */
  public SetInspectionService(SetInspectionRepository setInspectionRepository,
                SetInstanceMaterialRepository setInstanceMaterialRepository,
                SetBaseMaterialRepository setBaseMaterialRepository,
                SetInstanceRepository setInstanceRepository,
                UserRepository userRepository,
                SetReceiptRepository setReceiptRepository,
                SetInspectionDiscrepancyLineService setInspectionDiscrepancyLineService) {
    this.setInspectionRepository = setInspectionRepository;
    this.setInstanceMaterialRepository = setInstanceMaterialRepository;
    this.setBaseMaterialRepository = setBaseMaterialRepository;
    this.setInstanceRepository = setInstanceRepository;
    this.userRepository = userRepository;
    this.setReceiptRepository = setReceiptRepository;
    this.setInspectionDiscrepancyLineService = setInspectionDiscrepancyLineService;
  }

  /**
   * Performs a set inspection based on the most recent inbound receipt for the
   * specified set instance.
   * This method creates a new SetInspection record, associates it with the relevant SetReceipt, and
   * processes any discrepancies provided in the input DTO. If no discrepancies
   * are found, the inspection
   * is automatically closed.
   *
   * @param setTagId the tag ID of the set instance to be inspected
   * @param discrepancyListDto the DTO containing the list of discrepancies
   *                           identified during the inspection
   * @param comment an optional comment to be associated with the inspection
   * @param userEmail the email of the user performing the inspection, used
   *                 to identify the inspector
   * @throws ResourceNotFoundException if the set instance or related receipt cannot be found
   * @throws UnauthorizedException if the user performing the inspection cannot
   *      be found or is inactive
   */
  @Transactional
  public void performSetInspectionFromInboundReceipt(String setTagId,
                                    SetInspectionDiscrepancyListDto discrepancyListDto,
                                    String comment, String userEmail) {
    SetInstance setInstance = setInstanceRepository.findByTagId(setTagId)
        .orElseThrow(() -> new ResourceNotFoundException("Set with tag ID " + setTagId
          + " not found."));

    User user = userRepository.findByEmailAndActiveTrue(userEmail)
        .orElseThrow(() -> new UnauthorizedException("User with email " + userEmail
          + " not found or inactive."));

    SetReceipt setReceipt = setReceiptRepository
        .findTopBySetInstanceIdOrderByReceivedAtDesc(setInstance.getId())
        .orElseThrow(() -> new ResourceNotFoundException("No receipt found for set " + setTagId
          + ". Receive the set first."));

    SetInspection inspection = new SetInspection();
    inspection.setSetReceipt(setReceipt);
    inspection.setInspectedAt(LocalDateTime.now());
    inspection.setInspectedBy(user);
    inspection.setReceivedFromCustomer(null);
    inspection.setComment(comment);

    boolean noDiscrepancies = discrepancyListDto.getLines() == null
        || discrepancyListDto.getLines().isEmpty();

    if (noDiscrepancies) {
      inspection.setStatus(SetInspectionStatus.CLOSED);
      inspection.setClosedBy(user);
      inspection.setClosedAt(LocalDateTime.now());
      setInspectionRepository.save(inspection);
      return;
    }
    inspection.setStatus(SetInspectionStatus.OPEN);
    inspection = setInspectionRepository.save(inspection);

    for (SetInspectionDiscrepancyLineDto line : discrepancyListDto.getLines()) {
      setInspectionDiscrepancyLineService.createAndSaveSetInspectionDiscrepancyLine(
          comment, line, inspection);
    }

  }

  /**
   * Calculates the missing parts for a set instance identified by its tag ID.
   * This method retrieves the current materials present in the set instance and compares them
   * against the required materials defined in the set base. It returns a list of missing items
   * along with their required and present quantities.
   *
   * @param setTagId the tag ID of the set instance to calculate missing parts for
   * @return a list of SetMissingItemDto representing the missing items and their quantities
   * @throws ResourceNotFoundException if the set instance with the specified tag ID is not found
   */
  @Transactional
  public List<SetMissingItemDto> calculateMissingParts(String setTagId) {
    SetInstance setInstance = setInstanceRepository.findByTagId(setTagId).orElseThrow(
        () -> new ResourceNotFoundException("Set with tag ID " + setTagId + " not found."));
    List<SetInstanceMaterial> setInstanceMaterialList = setInstanceMaterialRepository
        .findBySetInstanceTagId(setTagId);
    List<SetBaseMaterial> setBaseMaterials = setBaseMaterialRepository
        .findBySetBaseCatalogNumber(setInstance.getSetBase().getCatalogNumber());
    Map<String, Long> presentQtyByRefNumber = setInstanceMaterialList.stream().collect(
        Collectors.groupingBy(setInstanceMaterial -> setInstanceMaterial.getItem().getRefNumber(),
        Collectors.summingLong(SetInstanceMaterial::getPresentQty)));
    List<SetMissingItemDto> result = new ArrayList<>();
    for (SetBaseMaterial setBaseMaterial : setBaseMaterials) {
      String refNumber = setBaseMaterial.getItem().getRefNumber();
      String itemName = setBaseMaterial.getItem().getName();
      long requiredQty = setBaseMaterial.getRequiredQty();
      Long presentQtyOfItemInSetInstanceMaterial = presentQtyByRefNumber
          .getOrDefault(refNumber, 0L);
      if (presentQtyOfItemInSetInstanceMaterial < requiredQty) {
        result.add(SetMissingItemDto.builder()
            .itemRefNumber(refNumber)
            .itemName(itemName)
            .requiredQty(requiredQty)
            .presentQty(presentQtyOfItemInSetInstanceMaterial)
            .missingQty(requiredQty - presentQtyOfItemInSetInstanceMaterial)
            .build());
      }
    }
    updateSetInstanceCompleteness(result, setInstance);
    return result;
  }

  /**
   * Updates the completeness status of the set instance based on the presence of missing items.
   *
   * @param missingItems  List of missing items identified during the inspection.
   * @param setInstance   The set instance being inspected.
   */
  public void updateSetInstanceCompleteness(List<SetMissingItemDto> missingItems,
                                             SetInstance setInstance) {
    boolean shouldBeComplete = missingItems.isEmpty();
    if (setInstance.isComplete() != shouldBeComplete) {
      setInstance.setComplete(shouldBeComplete);
      setInstanceRepository.save(setInstance);
    }
  }
}

