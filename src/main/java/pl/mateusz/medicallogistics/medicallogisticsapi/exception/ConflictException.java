package pl.mateusz.medicallogistics.medicallogisticsapi.exception;

/**
 * Custom exception class for handling conflict errors in the medical logistics API.
 * This exception is thrown when a request cannot be processed due to a conflict with
 * the current state of the resource,
 * such as attempting to create a resource that already exists or updating a resource
 * in a way that violates business rules.
 */
public class ConflictException extends RuntimeException {

  /**
   * Constructs a new ConflictException with the specified error message.
   *
   * @param message the detail message explaining the reason for the conflict
   */
  public ConflictException(String message) {
    super(message);
  }
}
