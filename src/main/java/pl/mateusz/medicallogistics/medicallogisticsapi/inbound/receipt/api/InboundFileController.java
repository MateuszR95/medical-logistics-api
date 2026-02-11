package pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.api;

import java.io.IOException;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.dto.InboundReceiptBatchDto;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.service.InboundReceiptImportService;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.service.InboundReceiptValidationService;
import pl.mateusz.medicallogistics.medicallogisticsapi.warehouse.service.InventoryService;

/**
 * REST controller for handling inbound receipt file uploads.
 */
@RestController
@RequestMapping("/api/inbound-files")
public class InboundFileController {

  private final InboundReceiptImportService storageService;
  private final InboundReceiptValidationService validationService;

  private final InventoryService inventoryService;

  /**
   * Constructs an InboundFileController with the specified services.
   *
   * @param storageService the service for handling file storage and batch creation
   * @param validationService the service for validating the contents of the uploaded file
   * @param inventoryService the service for managing inventory updates based on the uploaded data
   */
  public InboundFileController(InboundReceiptImportService storageService,
                               InboundReceiptValidationService validationService,
                               InventoryService inventoryService) {
    this.storageService = storageService;
    this.validationService = validationService;
    this.inventoryService = inventoryService;
  }

  /**
   * Handles the upload of an inbound receipt file.
   *
   * @param file the multipart file to be uploaded
   * @return a ResponseEntity containing the stored file name and upload status
   */
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> upload(@RequestPart("file") MultipartFile file,
                                  Authentication authentication) throws IOException {
    String email = authentication.getName();
    InboundReceiptBatchDto batch = storageService.importFileAndCreateBatch(file, email);
    validationService.processInboundReceiptFile(batch.getFileName());
    return ResponseEntity.ok(Map.of(
      "fileName", batch.getFileName(),
      "batchNumber", batch.getBatchNumber(),
      "batchId", batch.getId(),
      "status", batch.getStatus()
    ));
  }
}
