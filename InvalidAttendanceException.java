/**
 * Indicates that an attendance operation cannot be executed for the current state.
 */
public class InvalidAttendanceException extends Exception {
  /**
   * Creates an exception with a descriptive message.
   *
   * @param message the reason for the exception
   */
  public InvalidAttendanceException(String message) {
    super(message);
  }
}
