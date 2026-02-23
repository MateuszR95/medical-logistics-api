package pl.mateusz.medicallogistics.medicallogisticsapi.set.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mateusz.medicallogistics.medicallogisticsapi.config.InboundConfiguration;
import pl.mateusz.medicallogistics.medicallogisticsapi.exception.ResourceNotFoundException;
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

  /**
   * Retrieves a list of SetInstanceDto objects for all SetInstances with the specified SetStatus.
   * This method queries the database for SetInstances matching the given status, maps them to DTOs,
   * and returns the list of DTOs.
   *
   * @param setStatus the SetStatus to filter by
   * @return a List of SetInstanceDto objects representing the SetInstances
   *      with the specified status
   */
  public List<SetInstanceDto> findSetInstancesByStatus(SetStatus setStatus) {
    List<SetInstance> setInstances = setInstanceRepository.findBySetStatus(setStatus);
    return setInstances.stream()
        .map(SetInstanceDtoMapper::mapToDto)
        .collect(Collectors.toList());
  }

  /**
   * Retrieves a list of SetInstanceDto objects for all SetInstances in the database.
   * This method queries the database for all SetInstances, maps them to DTOs,
   * and returns the list of DTOs.
   *
   * @return a List of SetInstanceDto objects representing all SetInstances in the database
   */
  public List<SetInstanceDto> findAllSetInstances() {
    List<SetInstance> setInstances = setInstanceRepository.findAllBy();
    return setInstances.stream()
        .map(SetInstanceDtoMapper::mapToDto)
        .collect(Collectors.toList());
  }

  /**
   * Retrieves a list of SetInstanceDto objects for all SetInstances that are pending checking.
   * This method queries the database for SetInstances with statuses indicating
   * they are pending checking,
   * maps them to DTOs, and returns the list of DTOs.
   *
   * @return a List of SetInstanceDto objects representing the SetInstances
   *      that are pending checking
   */
  public List<SetInstanceDto> findAllSetsPendingChecking() {
    return setInstanceRepository.findAllSetInstancesPendingChecking()
      .stream().map(SetInstanceDtoMapper::mapToDto).collect(Collectors.toList());
  }

  public void moveSetInstanceToLocation(String setTagId, String locationCode) {

  }
}
