package pl.mateusz.medicallogistics.medicallogisticsapi.set.receipt.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.receipt.dto.SetReceiptDto;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.receipt.service.SetReceiptService;

/**
 * REST controller for managing set receipts in the medical logistics system.
 * This controller provides endpoints for handling operations related to set receipts,
 * such as creating new receipts, retrieving existing receipts, and updating receipt information.
 */
@RestController
@RequestMapping("/api/set-receipts")
public class SetReceiptController {

  private final SetReceiptService setReceiptService;

  /**
   * Constructs a new SetReceiptController with the specified SetReceiptService.
   *
   * @param setReceiptService the service for managing set receipts
   */
  public SetReceiptController(SetReceiptService setReceiptService) {
    this.setReceiptService = setReceiptService;
  }
  /**
   * Endpoint to initiate a new set receipt based on the provided set tag ID and
   * an optional comment.
   * This endpoint creates a new set receipt for the specified set tag ID and associates it with
   * the authenticated user.
   *
   * @param setTagId the tag ID of the set instance for which to create the receipt
   * @param authentication the authentication object containing the details
   *                       of the authenticated user
   * @param comment an optional comment to include with the receipt, with a maximum
   *                length of 1000 characters
   * @return a ResponseEntity containing the created SetReceiptDto and an HTTP status of CREATED
   */

  @PostMapping("/initiate-set-receipt")
  public ResponseEntity<SetReceiptDto> initiateSetReceipt(
      @RequestParam @NotBlank String setTagId,
      @RequestParam @NotBlank String locationCode,
      @RequestParam(required = false) @Size(max = 1000) String comment,
      Authentication authentication) {
    SetReceiptDto dto = setReceiptService.initiateSetReceiptFromCustomer(
        setTagId, locationCode, comment, authentication.getName());
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }



}
