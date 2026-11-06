import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single attendance session for one employee.
 */
public class AttendanceRecord {
  private final String employeeId;
  private final LocalDateTime timeIn;
  private LocalDateTime timeOut;

  /**
   * Creates a new attendance record with a starting time-in timestamp.
   *
   * @param employeeId the associated employee ID
   * @param timeIn the time-in timestamp
   */
  public AttendanceRecord(String employeeId, LocalDateTime timeIn) {
    this.employeeId = employeeId;
    this.timeIn = timeIn;
    this.timeOut = null;
  }

  /**
   * Returns the employee ID for this attendance record.
   *
   * @return the employee ID
   */
  public String getEmployeeId() {
    return employeeId;
  }

  /**
   * Returns the time-in timestamp.
   *
   * @return the time-in timestamp
   */
  public LocalDateTime getTimeIn() {
    return timeIn;
  }

  /**
   * Returns the current time-out timestamp.
   *
   * @return the time-out timestamp, or null if the record is still active
   */
  public LocalDateTime getTimeOut() {
    return timeOut;
  }

  /**
   * Sets the time-out timestamp for an attendance record.
   *
   * @param timeOut the time-out timestamp
   */
  public void setTimeOut(LocalDateTime timeOut) {
    this.timeOut = timeOut;
  }
}
