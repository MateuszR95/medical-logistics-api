package pl.mateusz.medicallogistics.medicallogisticsapi.set.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.domain.InboundReceiptLine;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.domain.Item;
import pl.mateusz.medicallogistics.medicallogisticsapi.lot.domain.Lot;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetInstance;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetInstanceMaterial;
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
}
