package pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mateusz.medicallogistics.medicallogisticsapi.consignment.items.request.service.ConsignmentItemRequestService;
import pl.mateusz.medicallogistics.medicallogisticsapi.exception.ResourceNotFoundException;
import pl.mateusz.medicallogistics.medicallogisticsapi.exception.UnauthorizedException;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetBaseMaterial;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetInstance;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetInstanceMaterial;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.SetInspectionStatus;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.SetInspectionDiscrepancyType;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.domain.SetInspectionDiscrepancyLine;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.dto.SetInspectionDiscrepancyLineDto;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.dto.SetInspectionDiscrepancyLineDtoMapper;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.dto.SetInspectionDiscrepancyListDto;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.service.SetInspectionDiscrepancyLineService;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.domain.SetInspection;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.dto.SetInspectionDto;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.dto.SetInspectionDtoMapper;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.dto.SetMissingItemDto;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.repository.SetInspectionRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.receipt.domain.SetReceipt;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.receipt.repository.SetReceiptRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.repository.SetBaseMaterialRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.repository.SetInstanceMaterialRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.repository.SetInstanceRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.service.SetInstanceMaterialService;
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
  private final SetInstanceMaterialService setInstanceMaterialService;

  private final ConsignmentItemRequestService consignmentItemRequestService;


  /**
   * Constructs a new instance of the SetInspectionService.
   *
   * @param setInspectionRepository the repository for managing SetInspection entities
   * @param setInstanceMaterialRepository the repository for managing SetInstanceMaterial entities
   * @param setBaseMaterialRepository the repository for managing SetBaseMaterial entities
   * @param setInstanceRepository the repository for managing SetInstance entities
   * @param userRepository the repository for managing User entities
   * @param setReceiptRepository the repository for managing SetReceipt entities
   * @param setInspectionDiscrepancyLineService the service for managing
   *                                           SetInspectionDiscrepancyLine entities
   * @param setInstanceMaterialService the service for managing SetInstanceMaterial entities
   * @param consignmentItemRequestService the service for managing consignment item requests
   */
  public SetInspectionService(SetInspectionRepository setInspectionRepository,
                              SetInstanceMaterialRepository setInstanceMaterialRepository,
                              SetBaseMaterialRepository setBaseMaterialRepository,
                              SetInstanceRepository setInstanceRepository,
                              UserRepository userRepository,
                              SetReceiptRepository setReceiptRepository,
                              SetInspectionDiscrepancyLineService
                                setInspectionDiscrepancyLineService,
                              SetInstanceMaterialService setInstanceMaterialService,
                              ConsignmentItemRequestService consignmentItemRequestService) {
    this.setInspectionRepository = setInspectionRepository;
    this.setInstanceMaterialRepository = setInstanceMaterialRepository;
    this.setBaseMaterialRepository = setBaseMaterialRepository;
    this.setInstanceRepository = setInstanceRepository;
    this.userRepository = userRepository;
    this.setReceiptRepository = setReceiptRepository;
    this.setInspectionDiscrepancyLineService = setInspectionDiscrepancyLineService;
    this.setInstanceMaterialService = setInstanceMaterialService;
    this.consignmentItemRequestService = consignmentItemRequestService;
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
  public SetInspectionDto performSetInspection(String setTagId,
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
    String setInspectionNumber = generateSetInspectionNumber();
    inspection.setSetInspectionNumber(setInspectionNumber);

    boolean noDiscrepancies = discrepancyListDto.getLines() == null
        || discrepancyListDto.getLines().isEmpty();

    if (noDiscrepancies) {
      inspection.setStatus(SetInspectionStatus.CLOSED);
      inspection.setClosedBy(user);
      inspection.setClosedAt(LocalDateTime.now());
      setInspectionRepository.save(inspection);
      return SetInspectionDtoMapper.mapToDto(inspection);
    }
    inspection.setStatus(SetInspectionStatus.OPEN);
    inspection = setInspectionRepository.save(inspection);
    Map<String, Long> missingPartsByRefNumber = new HashMap<>();
    for (SetInspectionDiscrepancyLineDto line : discrepancyListDto.getLines()) {
      SetInspectionDiscrepancyLine discrepancyLine =
          setInspectionDiscrepancyLineService.createAndSaveSetInspectionDiscrepancyLine(
          comment, line, inspection);
      inspection.addDiscrepancyLine(discrepancyLine);
      setInstanceMaterialService.updateSetInstanceMaterialByMissingDiscrepancyLine(setTagId,
          discrepancyLine);
      if (SetInspectionDiscrepancyType.MISSING.name().equals(line.getDiscrepancyType())) {
        missingPartsByRefNumber.merge(line.getItemRefNumber(), line.getQuantity(), Long::sum);
      }
    }
    if (!missingPartsByRefNumber.isEmpty()) {
      consignmentItemRequestService.initiateReplenishmentToSetInstance(
            userEmail, setInstance, inspection, missingPartsByRefNumber);
    }
    return SetInspectionDtoMapper.mapToDto(inspection);
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

  /**
   * Retrieves the list of discrepancies associated with a specific set inspection.
   *
   * @param setInspectionNumber the unique number identifying the set inspection
   * @return a list of SetInspectionDiscrepancyLineDto representing the discrepancies
   *      found during the inspection
   * @throws ResourceNotFoundException if the set inspection with the specified number is not found
   */
  public List<SetInspectionDiscrepancyLineDto> getSetInspectionDiscrepancies(
      String setInspectionNumber) {
    SetInspection setInspection = setInspectionRepository.findBySetInspectionNumber(
        setInspectionNumber)
        .orElseThrow(() -> new ResourceNotFoundException("Set inspection with number "
        + setInspectionNumber + " not found."));
    List<SetInspectionDiscrepancyLine> discrepancyLines = setInspection.getDiscrepancyLines();
    return discrepancyLines.stream()
      .map(SetInspectionDiscrepancyLineDtoMapper::mapToDto)
      .toList();
  }

  private String generateSetInspectionNumber() {
    return "SET-INSPECTION-" + LocalDate.now() + "-"
      + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
  }
}

