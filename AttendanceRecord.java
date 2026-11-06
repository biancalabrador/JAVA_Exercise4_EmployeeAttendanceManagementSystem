import java.time.LocalDateTime;

public class AttendanceRecord {
  private String employeeId;
  private LocalDateTime timeIn;
  private LocalDateTime timeOut;

  public AttendanceRecord(String employeeId, LocalDateTime timeIn) {
    this.employeeId = employeeId;
    this.timeIn = timeIn;
    this.timeOut = null;
  }

  public String getEmployeeId() {
    return employeeId;
  }

  public LocalDateTime getTimeIn() {
    return timeIn;
  }

  public LocalDateTime getTimeOut() {
    return timeOut;
  }

  public void setTimeOut(LocalDateTime timeOut) {
    this.timeOut = timeOut;
  }
}
