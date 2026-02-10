package pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.service;

import org.springframework.stereotype.Service;
import pl.mateusz.medicallogistics.medicallogisticsapi.config.InboundConfiguration;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.domain.Location;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.domain.Warehouse;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.repository.LocationRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.repository.WarehouseRepository;

/**
 * Service for managing locations within the warehouse in the medical logistics system.
 * This service is responsible for handling business logic related to warehouse locations,
 * such as creating, updating, and retrieving location information.
 */
@Service
public class LocationService {

  private final LocationRepository locationRepository;
  private final WarehouseRepository warehouseRepository;

  private final InboundConfiguration inboundConfiguration;

  /**
   * Constructs a LocationService with the specified repositories and configuration.
   *
   * @param locationRepository the repository for managing Location entities
   * @param warehouseRepository the repository for managing Warehouse entities
   * @param inboundConfiguration the configuration for inbound operations,
   *                            used to determine default locations
   */
  public LocationService(LocationRepository locationRepository,
                         WarehouseRepository warehouseRepository,
                         InboundConfiguration inboundConfiguration) {
    this.locationRepository = locationRepository;
    this.warehouseRepository = warehouseRepository;
    this.inboundConfiguration = inboundConfiguration;
  }

  /**
   * Retrieves the default receipt location based on the configuration.
   *
   * @return the default receipt Location
   * @throws IllegalArgumentException if the default receipt warehouse or location is not found
   */
  public Location getDefaultReceiptLocation() {
    Warehouse warehouse = warehouseRepository
        .findByCode(inboundConfiguration.getDefaultReceiptWarehouseCode())
        .orElseThrow(() -> new IllegalArgumentException(
        "Default receipt warehouse not found: "
          + inboundConfiguration.getDefaultReceiptWarehouseCode()));

    return locationRepository
      .findByWarehouseIdAndCode(warehouse.getId(),
        inboundConfiguration.getDefaultReceiptLocationCode())
      .orElseThrow(() -> new IllegalArgumentException(
        "Default receipt location not found: warehouse=" + warehouse.getCode()
          + ", location=" + inboundConfiguration.getDefaultReceiptLocationCode()));
  }
}
