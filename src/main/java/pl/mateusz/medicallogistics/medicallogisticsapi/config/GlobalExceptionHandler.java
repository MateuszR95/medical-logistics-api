package pl.mateusz.medicallogistics.medicallogisticsapi.config;

import java.io.IOException;
import java.time.Instant;
import lombok.Getter;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.mateusz.medicallogistics.medicallogisticsapi.exception.ConflictException;
import pl.mateusz.medicallogistics.medicallogisticsapi.exception.ResourceNotFoundException;

/**
 * Global exception handler for REST controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log =
      LoggerFactory.getLogger(GlobalExceptionHandler.class);

  /**
   * Handles bad request errors caused by invalid input data.
   */
  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex) {
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(ApiError.of(400, ex.getMessage()));
  }

  /**
   * Handles missing resources (e.g. item, lot, batch, location).
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex) {
    return ResponseEntity
      .status(HttpStatus.NOT_FOUND)
      .body(ApiError.of(404, ex.getMessage()));
  }

  /**
   * Handles business conflicts (e.g. duplicate inventory, duplicate identifiers).
   */
  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ApiError> handleConflict(ConflictException ex) {
    return ResponseEntity
      .status(HttpStatus.CONFLICT)
      .body(ApiError.of(409, ex.getMessage()));
  }

  /**
   * Handles database integrity violations (unique constraints, FK violations).
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex) {
    log.error("Data integrity violation", ex);
    return ResponseEntity
      .status(HttpStatus.CONFLICT)
      .body(ApiError.of(409, "Data conflict"));
  }

  /**
   * Fallback for IllegalArgumentException (treated as bad request).
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(ApiError.of(400, ex.getMessage()));
  }

  /**
   * Handles file processing errors.
   */
  @ExceptionHandler(IOException.class)
  public ResponseEntity<ApiError> handleIo(IOException ex) {
    log.error("File processing error", ex);
    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(ApiError.of(500, "File processing error"));
  }

  /**
   * Ultimate fallback for unexpected errors.
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleOther(Exception ex) {
    log.error("Unexpected error", ex);
    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(ApiError.of(500, "Unexpected error"));
  }

  /**
   * Standard API error response.
   */
  @Getter
  public static class ApiError {

    private final String timestamp;
    private final int status;
    private final String message;

    private ApiError(String timestamp, int status, String message) {
      this.timestamp = timestamp;
      this.status = status;
      this.message = message;
    }

    /**
     * Factory method to create an ApiError instance with the current timestamp.
     *
     * @param status  the HTTP status code
     * @param message the error message to include in the response
     * @return a new ApiError instance with the provided status and message
     */
    public static ApiError of(int status, String message) {
      return new ApiError(Instant.now().toString(), status, message);
    }
  }

}
