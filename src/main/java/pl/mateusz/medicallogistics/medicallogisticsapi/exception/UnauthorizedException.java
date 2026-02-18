package pl.mateusz.medicallogistics.medicallogisticsapi.exception;

/**
 * Custom exception class for handling unauthorized access errors in the medical logistics API.
 * This exception is thrown when a user attempts to access a resource or perform an action
 * for which they do not have the necessary permissions or authentication.
 */
public class UnauthorizedException extends RuntimeException {

  /**
   * Constructs a new UnauthorizedException with the specified error message.
   *
   * @param message the detail message explaining the reason for the unauthorized access
   */
  public UnauthorizedException(String message) {
    super(message);
  }
}
