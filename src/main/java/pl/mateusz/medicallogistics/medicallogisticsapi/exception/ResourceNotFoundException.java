package pl.mateusz.medicallogistics.medicallogisticsapi.exception;

/**
 * Custom exception class for handling resource not found errors in the medical logistics API.
 * This exception is thrown when a requested resource
 * (e.g. item, lot, batch, location) cannot be found
 * in the system, indicating that the client has made a request for a non-existent resource.
 */
public class ResourceNotFoundException extends RuntimeException {

  /**
   * Constructs a new ResourceNotFoundException with the specified error message.
   *
   * @param message the detail message explaining which resource was not found
   */
  public ResourceNotFoundException(String message) {
    super(message);
  }
}
