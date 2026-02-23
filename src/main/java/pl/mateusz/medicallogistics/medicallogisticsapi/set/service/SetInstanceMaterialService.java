package pl.mateusz.medicallogistics.medicallogisticsapi.set.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.domain.InboundReceiptLine;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.domain.Item;
import pl.mateusz.medicallogistics.medicallogisticsapi.lot.domain.Lot;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetInstance;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetInstanceMaterial;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.SetInspectionDiscrepancyType;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.domain.SetInspectionDiscrepancyLine;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.repository.SetBaseMaterialRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.repository.SetInstanceMaterialRepository;


/**
 * Service class for managing SetInstanceMaterial entities.
 * Provides business logic and operations related to SetInstanceMaterial.
 */
@Service
public class SetInstanceMaterialService {

  private final SetInstanceMaterialRepository setInstanceMaterialRepository;
  private final SetBaseMaterialRepository setBaseMaterialRepository;


  /**
   * Constructs a new SetInstanceMaterialService with the specified repositories.
   *
   * @param setInstanceMaterialRepository the repository for managing SetInstanceMaterial entities
   * @param setBaseMaterialRepository the repository for managing SetBaseMaterial entities
   */
  public SetInstanceMaterialService(SetInstanceMaterialRepository setInstanceMaterialRepository,
                                    SetBaseMaterialRepository setBaseMaterialRepository) {
    this.setInstanceMaterialRepository = setInstanceMaterialRepository;
    this.setBaseMaterialRepository = setBaseMaterialRepository;
  }

  /**
   * Creates or updates a SetInstanceMaterial based on the provided InboundReceiptLine.
   * This method is transactional to ensure data integrity during the creation or update process.
   *
   * @param inboundReceiptLine the InboundReceiptLine containing the details for creating
   *                          or updating the SetInstanceMaterial
   * @throws IllegalArgumentException if the InboundReceiptLine does not resolve to
   *      a valid SetInstance, Item, or Lot
   */
  @Transactional
  public void createAndSaveSetInstanceMaterialFromInboundReceiptLine(
      InboundReceiptLine inboundReceiptLine) {
    SetInstance setInstance = inboundReceiptLine.getResolvedSetInstance();
    Item item = inboundReceiptLine.getResolvedItem();
    Lot lot = inboundReceiptLine.getResolvedLot();
    if (setInstance == null || item == null || lot == null) {
      throw new IllegalArgumentException("SET line must resolve setInstance,"
        + " item and lot before saving set material.");
    }
    long qtyToAdd = inboundReceiptLine.getQty();
    SetInstanceMaterial setInstanceMaterial = setInstanceMaterialRepository
        .findBySetInstanceIdAndItemIdAndLotId(setInstance.getId(), item.getId(), lot.getId())
        .orElseGet(() -> {
          SetInstanceMaterial created = new SetInstanceMaterial();
          created.setSetInstance(setInstance);
          created.setItem(item);
          created.setLot(lot);
          created.setPresentQty(0L);
          created.setMissingQty(0L);
          return created;
        });
    setInstanceMaterial.setPresentQty(setInstanceMaterial.getPresentQty() + qtyToAdd);
    setInstanceMaterialRepository.save(setInstanceMaterial);
  }

  /**
   * Updates the SetInstanceMaterial for a given setTagId based on a missing discrepancy line.
   * This method is transactional to ensure data integrity during the update process.
   *
   * @param setTagId the tag ID of the set instance to update
   * @param discrepancyLine the SetInspectionDiscrepancyLine containing
   *                       the details of the missing item
   * @throws IllegalArgumentException if the discrepancy line is null, not of type MISSING,
   *      or does not have an associated item and lot
   * @throws IllegalStateException if no matching SetInstanceMaterial
   *      is found or if the update would
   *      result in negative present quantity
   */
  @Transactional
  public void updateSetInstanceMaterialByMissingDiscrepancyLine(
      String setTagId, SetInspectionDiscrepancyLine discrepancyLine) {
    if (discrepancyLine == null
        || discrepancyLine.getDiscrepancyType() != SetInspectionDiscrepancyType.MISSING) {
      return;
    }
    if (discrepancyLine.getItem() == null || discrepancyLine.getLot() == null) {
      throw new IllegalArgumentException("Discrepancy line must have item and lot.");
    }
    long qty = discrepancyLine.getQty();
    if (qty <= 0) {
      return;
    }
    List<SetInstanceMaterial> materials =
        setInstanceMaterialRepository.findBySetInstanceTagId(setTagId);
    List<SetInstanceMaterial> matched = materials.stream()
        .filter(m -> m.getItem().getId().equals(discrepancyLine.getItem().getId())
        && m.getLot().getLotNumber().equals(discrepancyLine.getLot().getLotNumber()))
        .toList();

    if (matched.isEmpty()) {
      throw new IllegalStateException("No SetInstanceMaterial found for setTagId=" + setTagId
        + ", item=" + discrepancyLine.getItem().getRefNumber()
        + ", lot=" + discrepancyLine.getLot().getLotNumber());
    }

    List<SetInstanceMaterial> toDelete = new ArrayList<>();
    List<SetInstanceMaterial> toSave = new ArrayList<>();

    for (SetInstanceMaterial material : matched) {
      long newPresent = material.getPresentQty() - qty;

      if (newPresent < 0) {
        throw new IllegalStateException("Present qty would go below zero for setTagId="
          + setTagId + ", item=" + material.getItem().getRefNumber()
          + ", lot=" + material.getLot().getLotNumber());
      }

      if (newPresent == 0) {
        toDelete.add(material);
      } else {
        material.setPresentQty(newPresent);
        material.setMissingQty(material.getMissingQty() + qty);
        toSave.add(material);
      }
    }

    if (!toDelete.isEmpty()) {
      setInstanceMaterialRepository.deleteAll(toDelete);
    }
    if (!toSave.isEmpty()) {
      setInstanceMaterialRepository.saveAll(toSave);
    }
  }
}
