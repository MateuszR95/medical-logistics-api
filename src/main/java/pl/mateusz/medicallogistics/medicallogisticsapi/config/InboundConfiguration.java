package pl.mateusz.medicallogistics.medicallogisticsapi.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for inbound processing settings.
 */
@Getter
@Configuration
public class InboundConfiguration {
  private final Path storageDir;

  @Value("${app.inbound.default-source-warehouse-code}")
  private String defaultSourceWarehouseCode;

  @Value("${app.inbound.default-receipt-warehouse-code}")
  private String defaultReceiptWarehouseCode;

  @Value("${app.inbound.default-receipt-location-code}")
  private String defaultReceiptLocationCode;

  /**
   * Constructs an InboundConfiguration with the specified storage directory.
   *
   * @param storageDir the directory where inbound files will be stored
   */
  public InboundConfiguration(
      @Value("${app.inbound.storage-dir}") String storageDir) {
    this.storageDir = Paths.get(storageDir).toAbsolutePath().normalize();
  }

}
