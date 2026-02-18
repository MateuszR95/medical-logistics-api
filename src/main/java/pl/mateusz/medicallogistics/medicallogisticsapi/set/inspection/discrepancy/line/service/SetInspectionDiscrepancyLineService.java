package pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mateusz.medicallogistics.medicallogisticsapi.exception.ResourceNotFoundException;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.domain.Item;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.repository.ItemRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.lot.domain.Lot;
import pl.mateusz.medicallogistics.medicallogisticsapi.lot.repository.LotRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.SetInspectionDiscrepancyResolution;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.SetInspectionDiscrepancyType;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.domain.SetInspectionDiscrepancyLine;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.dto.SetInspectionDiscrepancyLineDto;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.repository.SetInspectionDiscrepancyLineRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.domain.SetInspection;


/**
 * Service for managing SetInspectionDiscrepancyLine entities, which represent
 * individual discrepancy lines found during a set inspection. This service
 * provides methods for creating and saving discrepancy lines based on provided
 * data transfer objects (DTOs) and associating them with specific set inspections.
 */
@Service
public class SetInspectionDiscrepancyLineService {

  private final SetInspectionDiscrepancyLineRepository setInspectionDiscrepancyLineRepository;
  private final ItemRepository itemRepository;
  private final LotRepository lotRepository;

  /**
   * Constructs a new SetInspectionDiscrepancyLineService with the specified dependencies.
   *
   * @param setInspectionDiscrepancyLineRepository the repository for managing
   *                                               SetInspectionDiscrepancyLine entities
   * @param itemRepository the repository for managing Item entities
   * @param lotRepository the repository for managing Lot entities
   */
  public SetInspectionDiscrepancyLineService(
      SetInspectionDiscrepancyLineRepository setInspectionDiscrepancyLineRepository,
      ItemRepository itemRepository, LotRepository lotRepository) {
    this.setInspectionDiscrepancyLineRepository = setInspectionDiscrepancyLineRepository;
    this.itemRepository = itemRepository;
    this.lotRepository = lotRepository;
  }

  /**
   * Creates and saves a new SetInspectionDiscrepancyLine based on the provided DTO
   * and associates it with the given SetInspection.
   *
   * @param comment The comment to be associated with the discrepancy line.
   * @param discrepancyListDtoLine The DTO containing the details of the discrepancy
   *                              line to be created.
   * @param inspection The SetInspection to which the discrepancy line will be associated.
   * @throws ResourceNotFoundException if the item or lot specified in the DTO cannot be found.
   */
  @Transactional
  public void createAndSaveSetInspectionDiscrepancyLine(String comment,
                              SetInspectionDiscrepancyLineDto discrepancyListDtoLine,
                                                        SetInspection inspection) {
    SetInspectionDiscrepancyLine discrepancyLine = new SetInspectionDiscrepancyLine();
    discrepancyLine.setInspection(inspection);
    Item item = itemRepository.findByRefNumber(discrepancyListDtoLine.getItemRefNumber())
        .orElseThrow(() -> new ResourceNotFoundException(
        "Item with reference number " + discrepancyListDtoLine.getItemRefNumber()
          + " not found."));
    discrepancyLine.setItem(item);
    Lot lot = lotRepository.findByLotNumber(discrepancyListDtoLine.getLot()).orElseThrow(
        () -> new ResourceNotFoundException(
        "Lot with number " + discrepancyListDtoLine.getLot() + " not found."));
    discrepancyLine.setLot(lot);
    discrepancyLine.setQty(discrepancyListDtoLine.getQuantity());
    discrepancyLine.setResolution(SetInspectionDiscrepancyResolution.PENDING);
    discrepancyLine.setDiscrepancyType(SetInspectionDiscrepancyType.valueOf(
        discrepancyListDtoLine.getDiscrepancyType()));
    discrepancyLine.setLoanerRequest(null);
    discrepancyLine.setComment(comment);
    setInspectionDiscrepancyLineRepository.save(discrepancyLine);
  }
}
