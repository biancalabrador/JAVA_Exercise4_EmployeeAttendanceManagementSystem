package exceptions;
/**
 * Indicates that an employee record could not be found.
 */
public class EmployeeNotFoundException extends Exception {
  /**
   * Creates an exception with a descriptive message.
   *
   * @param message the reason for the exception
   */
  public EmployeeNotFoundException(String message) {
    super(message);
  }
}
