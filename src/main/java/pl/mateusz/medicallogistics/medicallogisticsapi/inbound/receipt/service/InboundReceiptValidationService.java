package pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.mateusz.medicallogistics.medicallogisticsapi.config.InboundConfiguration;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.domain.InboundReceiptBatch;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.dto.InboundReceiptLineDto;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.domain.Item;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.service.ItemService;
import pl.mateusz.medicallogistics.medicallogisticsapi.lot.domain.Lot;
import pl.mateusz.medicallogistics.medicallogisticsapi.lot.service.LotService;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.SetStatus;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.SetType;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetBase;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.domain.SetInstance;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.dto.SetInstanceDto;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.service.SetBaseService;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.service.SetInstanceService;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.service.InventoryService;

/**
 * Service for validating inbound receipt files and their contents.
 * This service reads receipt files, validates their format and content,
 * and logs any validation errors encountered during the process.
 */
@Service
public class InboundReceiptValidationService {

  private static final Logger logger =
      LoggerFactory.getLogger(InboundReceiptValidationService.class);

  private final Path storageDir;
  private final Validator validator;
  private final InboundReceiptLineService inboundReceiptLineService;

  private final InboundReceiptBatchService inboundReceiptBatchService;

  private final ItemService itemService;

  private final LotService lotService;

  private final SetInstanceService setInstanceService;
  private final SetBaseService setBaseService;

  private final InventoryService inventoryService;

  /**
   * Constructs an InboundReceiptValidationService with the specified dependencies.
   *
   * @param inboundConfiguration the configuration containing storage directory settings
   * @param validator the validator for validating DTOs
   * @param inboundReceiptLineService the service for managing inbound receipt lines
   * @param inboundReceiptBatchService the service for managing inbound receipt batches
   * @param itemService the service for managing items
   * @param lotService the service for managing lots
   * @param setInstanceService the service for managing set instances
   * @param setBaseService the service for managing set bases
   * @param inventoryService the service for managing inventory updates
   */
  public InboundReceiptValidationService(InboundConfiguration inboundConfiguration,
                                         Validator validator,
                                         InboundReceiptLineService inboundReceiptLineService,
                                         InboundReceiptBatchService inboundReceiptBatchService,
                                         ItemService itemService, LotService lotService,
                                         SetInstanceService setInstanceService,
                                         SetBaseService setBaseService,
                                         InventoryService inventoryService) {
    this.storageDir = inboundConfiguration.getStorageDir();
    this.validator = validator;
    this.inboundReceiptLineService = inboundReceiptLineService;
    this.inboundReceiptBatchService = inboundReceiptBatchService;
    this.itemService = itemService;
    this.lotService = lotService;
    this.setInstanceService = setInstanceService;
    this.setBaseService = setBaseService;
    this.inventoryService = inventoryService;
  }

  /**
   * Validates the receipt file with the given file name.
   *
   * @param fileName the name of the receipt file to validate
   * @throws IOException if an I/O error occurs while reading the file
   * @throws IllegalArgumentException if the file path is invalid or the file is not found
   */
  public void processInboundReceiptFile(String fileName) throws IOException {
    InboundReceiptLineDto inboundReceiptLineDto = null;
    InboundReceiptBatch inboundReceiptBatch = inboundReceiptBatchService
        .getInboundReceiptBatchByFileName(fileName);
    Path path = storageDir.resolve(fileName).normalize();
    if (!path.startsWith(storageDir)) {
      throw new IllegalArgumentException("Invalid file path");
    }
    if (!Files.exists(path)) {
      throw new IllegalArgumentException("File not found: " + fileName);
    }
    try (FileReader fileReader = new FileReader(path.toFile());
         BufferedReader reader = new BufferedReader(fileReader);
    ) {
      reader.readLine();
      String fileLine = null;
      while ((fileLine = reader.readLine()) != null) {

        String trimmedFileLine = fileLine.replaceAll("\\s", "");
        String[] fileLineItems = trimmedFileLine.split(",", -1);
        if (fileLineItems.length != 7) {
          logger.error("Invalid line format: {}", fileLine);
          saveWrongLineToErrorFile(trimmedFileLine, fileName);
          continue;
        }
        InboundReceiptLineDto receiptLineDto;
        try {
          receiptLineDto = mapToDtoFromLineItems(fileLineItems);
        } catch (RuntimeException ex) {
          logger.error("Invalid line (parsing failed): {} ({})", fileLine, ex.getMessage());
          saveWrongLineToErrorFile(fileLine, fileName);
          continue;
        }
        boolean isDtoValid = validateDto(receiptLineDto);
        if (!isDtoValid) {
          logger.error("Invalid line content: {}", fileLine);
          saveWrongLineToErrorFile(fileLine, fileName);
        } else {
          logger.info("Valid line: {}", fileLine);
          Item itemByRefNumber = itemService.getItemByRefNumber(receiptLineDto.getItemRefNumber());
          Optional<Lot> lotByLotNumberOptional = lotService.getLotByLotNumber(receiptLineDto
              .getLotNumber());
          LocalDate expirationDate = parseExpirationDateOrNull(receiptLineDto.getExpirationDate());
          Lot lot = lotByLotNumberOptional.orElseGet(() -> lotService.createAndSaveLot(
              receiptLineDto.getLotNumber(), itemByRefNumber, expirationDate));
          inboundReceiptLineDto = createAndSaveInboundReceiptLine(receiptLineDto,
            inboundReceiptBatch, itemByRefNumber, lot);
          inventoryService.updateInventoryAfterInboundReceipt(inboundReceiptLineDto);
        }
      }
    }
  }

  private InboundReceiptLineDto createAndSaveInboundReceiptLine(
      InboundReceiptLineDto receiptLineDto, InboundReceiptBatch inboundReceiptBatch,
      Item itemByRefNumber, Lot lot) {
    InboundReceiptLineDto inboundReceiptLineDto;
    if (receiptLineDto.getLineType().equals("SET")) {
      SetInstanceDto setInstanceDto = createSetInstanceDtoFromInboundReceiptLineDto(
          receiptLineDto);
      SetBase setBase = setBaseService.findByCatalogNumber(receiptLineDto
          .getSetCatalogNumber())
          .orElseThrow(() -> new IllegalArgumentException(
          "Set base not found for catalog number: " + receiptLineDto.getSetCatalogNumber()));
      SetInstance savedSetInstance = setInstanceService
          .createAndSaveSetInstanceFromInboundReceipt(setInstanceDto, setBase);
      inboundReceiptLineDto = inboundReceiptLineService.save(
        receiptLineDto, inboundReceiptBatch, itemByRefNumber,
        lot, savedSetInstance);
    } else {
      inboundReceiptLineDto = inboundReceiptLineService.save(receiptLineDto,
        inboundReceiptBatch, itemByRefNumber, lot, null);
    }
    return inboundReceiptLineDto;
  }

  private static LocalDate parseExpirationDateOrNull(String expirationDate) {
    if (expirationDate == null || expirationDate.isBlank()) {
      return null;
    }
    try {
      return LocalDate.parse(expirationDate.trim());
    } catch (DateTimeParseException ex) {
      logger.error("Invalid expiration date: {}", expirationDate);
      return null;
    }
  }

  private SetInstanceDto createSetInstanceDtoFromInboundReceiptLineDto(InboundReceiptLineDto dto) {
    SetInstanceDto setInstanceDto = new SetInstanceDto();
    setInstanceDto.setSetTagId(dto.getSetTagId());
    setInstanceDto.setSetType(SetType.CONSIGNMENT.name());
    setInstanceDto.setSetStatus(SetStatus.INBOUND.name());
    setInstanceDto.setActive(true);
    return setInstanceDto;
  }

  private void saveWrongLineToErrorFile(String fileLine, String fileName) throws IOException {
    String errorFileName = "errors_" + fileName;
    Path errorFilePath = storageDir.resolve(errorFileName);
    Files.createDirectories(errorFilePath.getParent());
    try (BufferedWriter writer = Files.newBufferedWriter(
        errorFilePath,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND
    )) {
      writer.write(fileLine);
      writer.newLine();
    }
  }

  private InboundReceiptLineDto mapToDtoFromLineItems(String[] fileLineItems) {
    return InboundReceiptLineDto.builder()
        .lineType(fileLineItems[0])
        .itemRefNumber(fileLineItems[1])
        .lotNumber(fileLineItems[2])
        .expirationDate(fileLineItems[3])
        .qty(Long.parseLong(fileLineItems[4]))
        .setCatalogNumber(fileLineItems[5])
        .setTagId(fileLineItems[6])
        .build();
  }

  private boolean validateDto(InboundReceiptLineDto dto) {
    Set<ConstraintViolation<InboundReceiptLineDto>> constraintViolations = validator.validate(dto);
    if (!constraintViolations.isEmpty()) {
      System.out.println("Line has validation errors:");
      for (ConstraintViolation<InboundReceiptLineDto> violation : constraintViolations) {
        logger.error("Validation error: {} {} ({})",
            violation.getPropertyPath(),
            violation.getMessage(),
            violation.getInvalidValue());
      }
      return false;
    }
    return true;
  }

}