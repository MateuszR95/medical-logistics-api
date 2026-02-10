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

/**
 * REST controller for handling inbound receipt file uploads.
 */
@RestController
@RequestMapping("/api/inbound-files")
public class InboundFileController {

  private final InboundReceiptImportService storageService;
  private final InboundReceiptValidationService validationService;

  /**
   * Constructs a new InboundFileController with the specified services.
   *
   * @param storageService the service responsible for importing and storing inbound receipt files
   * @param validationService the service responsible for validating inbound receipt files
   */
  public InboundFileController(InboundReceiptImportService storageService,
                               InboundReceiptValidationService validationService) {
    this.storageService = storageService;
    this.validationService = validationService;
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
    validationService.validateReceiptFile(batch.getFileName());
    return ResponseEntity.ok(Map.of(
      "fileName", batch.getFileName(),
      "batchNumber", batch.getBatchNumber(),
      "batchId", batch.getId(),
      "status", batch.getStatus()
    ));
  }
}
