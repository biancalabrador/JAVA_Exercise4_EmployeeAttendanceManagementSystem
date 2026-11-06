import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Runs a series of automated checks to verify attendance-management behavior.
 */
public class Main {
  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  /**
   * Executes the attendance-system test suite.
   *
   * @param args command-line arguments (unused)
   */
  public static void main(String[] args) {
    AttendanceManager attendanceManager = new AttendanceManager();

    Employee riri = new Employee("E001", "Riri Williams", "IT");
    Employee nick = new Employee("E002", "Nick Fury", "HR");

    // Test Case 1: Register employees.
    printCase(1, "Register employees");
    System.out.println("ID\tName\t\t\tDepartment");
    System.out.println("E001\tRiri Williams\t\tIT");
    System.out.println("E002\tNick Fury\t\tHR");
    try {
      attendanceManager.registerEmployee(riri);
      attendanceManager.registerEmployee(nick);
      System.out.println("The employees were successfully registered.");
    } catch (EmployeeAlreadyExistsException e) {
      throw new AssertionError("Unexpected duplicate employee during registration.", e);
    }

    // Test Case 2: Prevent duplicate employee registration.
    printCase(2, "Prevent duplicate employee registration");
    try {
      attendanceManager.registerEmployee(new Employee("E001", "Thor Son of Odin", "Electrical"));
      throw new AssertionError("Duplicate registration was not rejected.");
    } catch (EmployeeAlreadyExistsException e) {
      System.out.println("EmployeeAlreadyExistsException thrown and handled: " + e.getMessage());
    }

    // Test Case 3: Find an existing employee.
    printCase(3, "Find an existing employee");
    try {
      Employee found = attendanceManager.findEmployeeById("E001");
      require(found.getName().equals("Riri Williams"), "Unexpected employee returned for E001.");
      System.out.println(
          "Found employee: " + found.getEmployeeId() + " - " + found.getName()
              + " - " + found.getDepartment());
    } catch (EmployeeNotFoundException e) {
      throw new AssertionError("Registered employee E001 was not found.", e);
    }

    // Test Case 4: Handle an unregistered employee.
    printCase(4, "Handle an unregistered employee");
    try {
      attendanceManager.findEmployeeById("E999");
      throw new AssertionError("Lookup of unregistered employee E999 was not rejected.");
    } catch (EmployeeNotFoundException e) {
      System.out.println("EmployeeNotFoundException thrown and handled: " + e.getMessage());
    }

    // Test Case 5: Record an employee's time-in.
    printCase(5, "Record an employee's time-in");
    LocalDateTime timeInStartedAt = LocalDateTime.now();
    try {
      attendanceManager.timeIn("E001");
      System.out.println("Time-in recorded for E001; the attendance record is active.");
    } catch (EmployeeNotFoundException | InvalidAttendanceException e) {
      throw new AssertionError("Could not record the initial time-in for E001.", e);
    }
    LocalDateTime timeInFinishedAt = LocalDateTime.now();

    // Test Case 6: Prevent multiple active attendance records.
    printCase(6, "Prevent multiple active attendance records");
    try {
      attendanceManager.timeIn("E001");
      throw new AssertionError("A second active attendance record was created for E001.");
    } catch (InvalidAttendanceException e) {
      System.out.println("InvalidAttendanceException thrown and handled: " + e.getMessage());
    } catch (EmployeeNotFoundException e) {
      throw new AssertionError("Registered employee E001 was not found.", e);
    }

    // Test Case 7: Prevent time-out without an active record.
    printCase(7, "Prevent time-out without an active record");
    try {
      attendanceManager.timeOut("E002");
      throw new AssertionError("Time-out without an active record was not rejected for E002.");
    } catch (InvalidAttendanceException e) {
      System.out.println("InvalidAttendanceException thrown and handled: " + e.getMessage());
    } catch (EmployeeNotFoundException e) {
      throw new AssertionError("Registered employee E002 was not found.", e);
    }

    // Test Case 8: Record an employee's time-out.
    printCase(8, "Record an employee's time-out");
    LocalDateTime timeOutStartedAt = LocalDateTime.now();
    try {
      attendanceManager.timeOut("E001");
    } catch (EmployeeNotFoundException | InvalidAttendanceException e) {
      throw new AssertionError("Could not record the time-out for E001.", e);
    }
    LocalDateTime timeOutFinishedAt = LocalDateTime.now();
    List<AttendanceRecord> completedRecords;
    try {
      completedRecords = attendanceManager.getCompletedAttendanceRecords("E001");
    } catch (EmployeeNotFoundException e) {
      throw new AssertionError("Registered employee E001 was not found.", e);
    }
    require(completedRecords.size() == 1, "Expected exactly one completed record for E001.");
    AttendanceRecord firstRecord = completedRecords.get(0);
    require(
        !firstRecord.getTimeIn().isBefore(timeInStartedAt)
            && !firstRecord.getTimeIn().isAfter(timeInFinishedAt),
        "The first record's time-in was not recorded at the current time.");
    require(
        firstRecord.getTimeOut() != null
            && !firstRecord.getTimeOut().isBefore(timeOutStartedAt)
            && !firstRecord.getTimeOut().isAfter(timeOutFinishedAt),
        "The first record's time-out was not updated at the current time.");
    LocalDateTime firstRecordTimeOut = firstRecord.getTimeOut();
    System.out.println("The active attendance record was updated with the current time-out.");

    // Test Case 9: Allow a new attendance record after completing a previous one.
    printCase(9, "Allow a new attendance record after completing a previous one");
    try {
      attendanceManager.timeIn("E001");
    } catch (EmployeeNotFoundException | InvalidAttendanceException e) {
      throw new AssertionError("Could not start a new attendance record for E001.", e);
    }
    try {
      List<AttendanceRecord> recordsAfterSecondTimeIn =
          attendanceManager.getCompletedAttendanceRecords("E001");
      require(
          recordsAfterSecondTimeIn.size() == 1
              && recordsAfterSecondTimeIn.get(0) == firstRecord
              && recordsAfterSecondTimeIn.get(0).getTimeOut().equals(firstRecordTimeOut),
          "The new time-in changed the previous completed record.");
      System.out.println(
          "A new active record was created; the previous completed record is unchanged.");
    } catch (EmployeeNotFoundException e) {
      throw new AssertionError("Registered employee E001 was not found.", e);
    }

    // Test Case 10: Display only completed attendance records.
    printCase(10, "Display completed attendance records for E001");
    try {
      completedRecords = attendanceManager.getCompletedAttendanceRecords("E001");
    } catch (EmployeeNotFoundException e) {
      throw new AssertionError("Registered employee E001 was not found.", e);
    }
    require(
        completedRecords.size() == 1 && completedRecords.get(0) == firstRecord,
        "Completed-record filtering included an active record or omitted a completed record.");
    for (AttendanceRecord record : completedRecords) {
      System.out.println(
          "Employee ID: " + record.getEmployeeId()
              + ", Time In: " + record.getTimeIn().format(DATE_TIME_FORMATTER)
              + ", Time Out: " + record.getTimeOut().format(DATE_TIME_FORMATTER));
    }
    System.out.println("The active attendance record was excluded.");
  }

  /**
   * Prints a numbered test section heading.
   *
   * @param number the case number
   * @param description the test description
   */
  private static void printCase(int number, String description) {
    System.out.println("\nTest " + number + ": " + description);
  }

  /**
   * Throws an assertion error when a condition is false.
   *
   * @param condition the condition to validate
   * @param message the assertion message
   */
  private static void require(boolean condition, String message) {
    if (!condition) {
      throw new AssertionError(message);
    }
  }
}
