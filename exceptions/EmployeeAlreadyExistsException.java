package exceptions;
/**
 * Indicates that an employee ID is already present in the system.
 */
public class EmployeeAlreadyExistsException extends Exception {
  /**
   * Creates an exception with a descriptive message.
   *
   * @param message the reason for the exception
   */
  public EmployeeAlreadyExistsException(String message) {
    super(message);
  }
}
