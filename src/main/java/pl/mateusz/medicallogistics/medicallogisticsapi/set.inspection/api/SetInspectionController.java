package pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.api;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.mateusz.medicallogistics.medicallogisticsapi.exception.ResourceNotFoundException;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.dto.SetInspectionDiscrepancyListDto;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.dto.SetMissingItemDto;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.service.SetInspectionService;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.repository.SetInstanceRepository;

/**
 * REST controller for managing set inspections.
 * This controller provides endpoints for performing inspections on sets and
 * calculating missing parts.
 */
@RestController
@RequestMapping("/api/set-inspections")
public class SetInspectionController {

  private final SetInspectionService setInspectionService;
  private final SetInstanceRepository setInstanceRepository;

  /**
   * Constructs a new SetInspectionController with the specified dependencies.
   *
   * @param setInspectionService the service for managing set inspections
   * @param setInstanceRepository the repository for accessing set instances
   */
  public SetInspectionController(SetInspectionService setInspectionService,
                                 SetInstanceRepository setInstanceRepository) {
    this.setInspectionService = setInspectionService;
    this.setInstanceRepository = setInstanceRepository;
  }

  /**
   * Endpoint to calculate the missing parts for a set instance identified by its tag ID.
   *
   * @param setTagId the tag ID of the set instance to calculate missing parts for
   * @return a ResponseEntity containing a list of SetMissingItemDto representing the missing
   *      items and their quantities
   * @throws ResourceNotFoundException if the set instance with the specified tag ID is not found
   */
  @GetMapping("/missing-parts")
  public ResponseEntity<List<SetMissingItemDto>> getMissingPartsInSet(@RequestParam(
      name = "setTagId") String setTagId) {
    return ResponseEntity.ok(setInspectionService.calculateMissingParts(setTagId));
  }

  /**
   * Endpoint to perform a set inspection based on an inbound receipt. This endpoint accepts
   * the set tag ID, an optional comment, and a list of discrepancies found during the inspection.
   *
   * @param setTagId the tag ID of the set instance being inspected
   * @param comment an optional comment to be associated with the inspection
   * @param lines a list of discrepancies found during the inspection, provided in the request body
   * @param authentication the authentication object containing details of the authenticated user
   * @return a ResponseEntity indicating the result of the operation
   */
  @PostMapping("/perform-inspection")
  public ResponseEntity<?> performSetInspectionFromInboundReceipt(@RequestParam String setTagId,
                               @RequestParam(required = false) String comment,
                               @RequestBody SetInspectionDiscrepancyListDto lines,
                               Authentication authentication) {
    setInspectionService.performSetInspectionFromInboundReceipt(setTagId, lines,
        comment, authentication.getName());
    return ResponseEntity.ok().build();
  }


}
