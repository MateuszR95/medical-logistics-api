package pl.mateusz.medicallogistics.medicallogisticsapi.config;

import java.io.IOException;
import java.time.Instant;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for REST controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles IllegalArgumentException and returns a 400 Bad Request response.
   *
   * @param ex the exception
   * @return the response entity with error details
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiError> handleBadRequest(IllegalArgumentException ex) {
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(new ApiError(Instant.now().toString(), 400, ex.getMessage()));
  }

  /**
   * Handles IOException and returns a 500 Internal Server Error response.
   *
   * @param ex the exception
   * @return the response entity with error details
   */
  @ExceptionHandler(IOException.class)
  public ResponseEntity<ApiError> handleIo(IOException ex) {
    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(new ApiError(Instant.now().toString(), 500, "File processing error"));
  }

  /**
   * Handles all other exceptions and returns a 500 Internal Server Error response.
   *
   * @param ex the exception
   * @return the response entity with error details
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleOther(Exception ex) {
    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(new ApiError(Instant.now().toString(), 500, "Unexpected error"));
  }


  /**
   * Class representing an API error response.
   */
  @Getter
  public static class ApiError {
    private final String timestamp;
    private final int status;
    private final String message;

    /**
     * Constructs an ApiError with the specified timestamp, status, and message.
     *
     * @param timestamp the timestamp of the error
     * @param status    the HTTP status code
     * @param message   the error message
     */
    public ApiError(String timestamp, int status, String message) {
      this.timestamp = timestamp;
      this.status = status;
      this.message = message;
    }

  }

}
