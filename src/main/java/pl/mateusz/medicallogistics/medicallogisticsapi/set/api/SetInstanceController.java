package pl.mateusz.medicallogistics.medicallogisticsapi.set.api;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.SetStatus;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.dto.SetInstanceDto;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.service.SetInstanceService;

/**
 * REST controller for managing set instances in the medical logistics system.
 * This controller provides endpoints for retrieving set instance information based on their status.
 */
@RestController
@RequestMapping("/api/set-instances")
public class SetInstanceController {

  private final SetInstanceService setInstanceService;

  /**
   * Constructs a new SetInstanceController with the specified SetInstanceService.
   *
   * @param setInstanceService the service for managing set instances, providing
   *                           business logic and data access functionality
   */
  public SetInstanceController(SetInstanceService setInstanceService) {
    this.setInstanceService = setInstanceService;
  }

  /**
   * Retrieves a list of SetInstanceDto objects based on the specified SetStatus.
   * This endpoint allows clients to filter set instances by their status, such as
   * ACTIVE, INACTIVE, or MAINTENANCE.
   *
   * @param status the SetStatus to filter set instances by, provided as a query parameter
   * @return a ResponseEntity containing a list of SetInstanceDto
   *      objects matching the specified status
   */
  @GetMapping
  public ResponseEntity<List<SetInstanceDto>> getSetInstancesByStatus(
      @RequestParam(name = "status", required = false) SetStatus status) {
    List<SetInstanceDto> result;
    if (status == null) {
      result = setInstanceService.findAllSetInstances();
    } else {
      result = setInstanceService.findSetInstancesByStatus(status);
    }
    return ResponseEntity.ok(result);
  }

  /**
   * Retrieves a list of SetInstanceDto objects that are pending checking.
   * This endpoint allows clients to get all set instances that are currently
   * in a pending checking status.
   *
   * @return a ResponseEntity containing a list of SetInstanceDto objects that are pending checking
   */
  @GetMapping("/pending-checking")
  public ResponseEntity<List<SetInstanceDto>> getSetInstancesPendingChecking() {
    List<SetInstanceDto> result = setInstanceService.findAllSetsPendingChecking();
    return ResponseEntity.ok(result);
  }

}
