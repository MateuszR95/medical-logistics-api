package pl.mateusz.medicallogistics.medicallogisticsapi.exception;

/**
 * Custom exception class for handling bad request errors in the medical logistics API.
 * This exception is thrown when the client sends invalid input data or requests an operation
 * that cannot be performed due to business rules or validation failures.
 */
public class BadRequestException extends RuntimeException {

  /**
   * Constructs a new BadRequestException with the specified error message.
   *
   * @param message the detail message explaining the reason for the bad request
   */
  public BadRequestException(String message) {
    super(message);
  }
}
