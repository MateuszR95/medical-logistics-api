package pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.mateusz.medicallogistics.medicallogisticsapi.config.InboundConfiguration;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.InboundReceiptBatchStatus;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.domain.InboundReceiptBatch;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.dto.InboundReceiptBatchDto;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.dto.InboundReceiptBatchDtoMapper;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.repository.InboundReceiptBatchRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.user.domain.User;
import pl.mateusz.medicallogistics.medicallogisticsapi.user.repository.UserRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.domain.Warehouse;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.repository.WarehouseRepository;

/**
 * Service for handling the storage of inbound receipt files.
 */
@Service
public class InboundReceiptImportService {

  private final Path storageDir;
  Logger logger = LoggerFactory.getLogger(InboundReceiptImportService.class);

  private final InboundReceiptBatchRepository inboundReceiptBatchRepository;

  private final WarehouseRepository warehouseRepository;

  private final InboundConfiguration inboundConfiguration;

  private final UserRepository userRepository;

  /**
   * Constructs an InboundReceiptImportService with the specified inbound configuration.
   *
   * @param inboundConfiguration the configuration containing storage directory settings
   */
  public InboundReceiptImportService(InboundConfiguration inboundConfiguration,
                                     InboundReceiptBatchRepository inboundReceiptBatchRepository,
                                     WarehouseRepository warehouseRepository,
                                     UserRepository userRepository) {
    this.inboundConfiguration = inboundConfiguration;
    this.storageDir = inboundConfiguration.getStorageDir();
    this.inboundReceiptBatchRepository = inboundReceiptBatchRepository;
    this.warehouseRepository = warehouseRepository;
    this.userRepository = userRepository;
  }

  /**
   * Imports the given receipt file, stores it, and creates a batch record for it.
   *
   * @param file      the multipart file to be imported
   * @param userEmail the email of the user performing the import
   * @return an InboundReceiptBatchDto representing the created batch record
   * @throws IllegalArgumentException if the file is null or empty
   * @throws IllegalStateException if there is an error during file storage or batch record creation
   */
  @Transactional
  public InboundReceiptBatchDto importFileAndCreateBatch(MultipartFile file, String userEmail) {
    String storedFileName = storeReceiptFile(file);
    return createAndSaveBatchRecord(storedFileName, userEmail);
  }

  /**
   * Stores the given receipt file in the configured storage directory.
   *
   * @param file the multipart file to be stored
   * @return the name under which the file was stored
   * @throws IllegalArgumentException if the file is null or empty
   * @throws IllegalStateException    if there is an error during file storage
   */
  private String storeReceiptFile(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException("file is required");
    }
    try {
      Files.createDirectories(storageDir);

      LocalDate today = LocalDate.now(ZoneId.of("Europe/Warsaw"));
      String datePart = today.format(DateTimeFormatter.BASIC_ISO_DATE);

      int nextNo = nextNumberForDay(datePart);

      String storedName = datePart + "_" + nextNo + ".csv";
      Path target = storageDir.resolve(storedName).normalize();

      if (!target.startsWith(storageDir)) {
        throw new IllegalStateException("Invalid stored file path");
      }

      try (InputStream in = file.getInputStream()) {
        Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
      }
      return storedName;
    } catch (IOException e) {
      throw new IllegalStateException("Cannot store file", e);
    }
  }


  private synchronized int nextNumberForDay(String datePart) throws IOException {
    int max = 0;

    try (DirectoryStream<Path> stream =
           Files.newDirectoryStream(storageDir, datePart + "_*.csv")) {

      for (Path p : stream) {
        String name = p.getFileName().toString();
        int underscore = name.indexOf('_');
        int dot = name.lastIndexOf('.');

        if (underscore < 0 || dot < 0) {
          continue;
        }
        try {
          int n = Integer.parseInt(name.substring(underscore + 1, dot));
          if (n > max) {
            max = n;
          }
        } catch (NumberFormatException ignore) {
          logger.debug("Skipping file with invalid daily sequence number: {}", name);
        }
      }
    }
    return max + 1;
  }

  /**
   * Creates and saves a batch record for the imported receipt file.
   *
   * @param storedFileName the name of the stored file
   * @param userEmail       the name of the user who uploaded the file
   * @return the created InboundReceiptBatchDto representing the batch record
   * @throws IllegalStateException if there is an error during batch record creation or saving
   */
  private InboundReceiptBatchDto createAndSaveBatchRecord(String storedFileName, String userEmail) {
    InboundReceiptBatchDto batchDto = new InboundReceiptBatchDto();
    Warehouse receiptWarehouse = warehouseRepository
        .findByCode(inboundConfiguration.getDefaultReceiptWarehouseCode())
        .orElseThrow(() -> new IllegalStateException("Default receipt warehouse not found"));
    User user = userRepository.findByEmailAndActiveTrue(userEmail)
        .orElseThrow(() -> new IllegalStateException("User not found"));
    batchDto.setBatchNumber("BATCH-" + LocalDateTime.now(ZoneId.of("Europe/Warsaw"))
        .format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")));
    batchDto.setFileName(storedFileName);
    batchDto.setSourceWarehouseCode(inboundConfiguration.getDefaultSourceWarehouseCode());
    batchDto.setStatus(InboundReceiptBatchStatus.IMPORTED.name());
    batchDto.setCreatedAt(LocalDateTime.now(ZoneId.of("Europe/Warsaw")));
    batchDto.setCreatedById(user.getId());
    InboundReceiptBatch inboundReceiptBatch = InboundReceiptBatchDtoMapper
        .mapToEntity(batchDto, receiptWarehouse, user);
    InboundReceiptBatch savedBatch = inboundReceiptBatchRepository.save(inboundReceiptBatch);
    return InboundReceiptBatchDtoMapper.mapToDto(savedBatch);
  }
}


