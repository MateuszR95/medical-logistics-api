package pl.mateusz.medicallogistics.medicallogisticsapi.set.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mateusz.medicallogistics.medicallogisticsapi.config.InboundConfiguration;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.SetStatus;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.SetType;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetBase;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetInstance;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.dto.SetInstanceDto;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.dto.SetInstanceDtoMapper;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.repository.SetInstanceRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.domain.Location;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.domain.Warehouse;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.repository.LocationRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.repository.WarehouseRepository;

/**
 * Service for managing set instances in the medical logistics system.
 * This service is responsible for handling business logic related to set instances,
 * such as creating, updating, and retrieving set instance information.
 */
@Service
public class SetInstanceService {

  private final SetInstanceRepository setInstanceRepository;
  private final InboundConfiguration inboundConfiguration;
  private final LocationRepository locationRepository;
  private final WarehouseRepository warehouseRepository;

  /**
   * Constructs a new SetInstanceService with the specified dependencies.
   *
   * @param setInstanceRepository the repository for managing SetInstance entities
   * @param inboundConfiguration the configuration for inbound processing,
   *                            providing necessary settings
   * @param locationRepository the repository for managing Location entities
   * @param warehouseRepository the repository for managing Warehouse entities
   */
  public SetInstanceService(SetInstanceRepository setInstanceRepository,
                            InboundConfiguration inboundConfiguration,
                            LocationRepository locationRepository,
                            WarehouseRepository warehouseRepository) {
    this.setInstanceRepository = setInstanceRepository;
    this.inboundConfiguration = inboundConfiguration;
    this.locationRepository = locationRepository;
    this.warehouseRepository = warehouseRepository;
  }

  /**
   * Creates and saves a new SetInstance based on the provided SetInstanceDto and SetBase.
   * This method maps the DTO to an entity, assigns a default location for new sets,
   * and saves the entity to the database.
   *
   * @param setInstanceDto the DTO containing data for the new SetInstance
   * @param setBase the SetBase entity associated with the new SetInstance
   * @return a SetInstanceDto representing the saved SetInstance
   */
  @Transactional
  public SetInstance createAndSaveSetInstanceFromInboundReceipt(SetInstanceDto setInstanceDto,
                                    SetBase setBase) {
    Warehouse receiptWarehouse = warehouseRepository
        .findByCode(inboundConfiguration.getDefaultReceiptWarehouseCode())
        .orElseThrow(() -> new IllegalStateException(
        "Default receipt warehouse with code "
          + inboundConfiguration.getDefaultReceiptWarehouseCode() + " not found"));
    Location defaultLocationForNewSets = locationRepository
        .findByWarehouseIdAndCode(receiptWarehouse.getId(),
        inboundConfiguration.getDefaultReceiptLocationCode())
        .orElseThrow(() -> new IllegalStateException(
        "Default receipt location with code "
          + inboundConfiguration.getDefaultReceiptLocationCode()
          + " not found in warehouse " + receiptWarehouse.getCode()));
    if (setInstanceRepository.existsByTagId(setInstanceDto.getSetTagId())) {
      throw new IllegalArgumentException("SetInstance with tagId already exists: "
        + setInstanceDto.getSetTagId());
    }
    SetInstance setInstance = new SetInstance();
    setInstance.setTagId(setInstanceDto.getSetTagId());
    setInstance.setLocation(defaultLocationForNewSets);
    setInstance.setSetBase(setBase);
    setInstance.setSetType(SetType.CONSIGNMENT);
    setInstance.setSetStatus(SetStatus.INBOUND);
    setInstance.setActive(true);
    return setInstanceRepository.save(setInstance);
  }
}
