import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import exceptions.EmployeeAlreadyExistsException;
import exceptions.EmployeeNotFoundException;
import exceptions.InvalidAttendanceException;

/**
 * Manages employee registration and attendance tracking for the attendance system.
 */
public class AttendanceManager {
  private final List<Employee> employees;
  private final List<AttendanceRecord> attendanceRecords;

  /**
   * Creates a new attendance manager with empty employee and attendance lists.
   */
  public AttendanceManager() {
    employees = new ArrayList<>();
    attendanceRecords = new ArrayList<>();
  }

  /**
   * Registers a new employee if the employee ID is not already in use.
   *
   * @param employee the employee to register
   * @throws EmployeeAlreadyExistsException if the employee ID already exists
   */
  public void registerEmployee(Employee employee) throws EmployeeAlreadyExistsException {
    for (Employee registeredEmployee : employees) {
      if (Objects.equals(registeredEmployee.getEmployeeId(), employee.getEmployeeId())) {
        throw new EmployeeAlreadyExistsException(
            "Employee with ID " + employee.getEmployeeId() + " already exists.");
      }
    }
    employees.add(employee);
  }

  /**
   * Finds an employee by their unique employee ID.
   *
   * @param employeeId the employee ID to look up
   * @return the matching employee
   * @throws EmployeeNotFoundException if no employee matches the provided ID
   */
  public Employee findEmployeeById(String employeeId) throws EmployeeNotFoundException {
    for (Employee employee : employees) {
      if (Objects.equals(employee.getEmployeeId(), employeeId)) {
        return employee;
      }
    }
    throw new EmployeeNotFoundException("Employee with ID " + employeeId + " was not found.");
  }

  /**
   * Records the employee's time-in event if they are currently not checked in.
   *
   * @param employeeId the employee ID to check in
   * @throws EmployeeNotFoundException if the employee does not exist
   * @throws InvalidAttendanceException if the employee already has an active attendance record
   */
  public void timeIn(String employeeId)
      throws EmployeeNotFoundException, InvalidAttendanceException {
    findEmployeeById(employeeId);

    for (AttendanceRecord record : attendanceRecords) {
      if (Objects.equals(record.getEmployeeId(), employeeId) && record.getTimeOut() == null) {
        throw new InvalidAttendanceException(
            "Employee with ID " + employeeId + " already has an active attendance record.");
      }
    }

    attendanceRecords.add(new AttendanceRecord(employeeId, LocalDateTime.now()));
  }

  /**
   * Records the employee's time-out event for the latest active attendance record.
   *
   * @param employeeId the employee ID to check out
   * @throws EmployeeNotFoundException if the employee does not exist
   * @throws InvalidAttendanceException if the employee has no active attendance record
   */
  public void timeOut(String employeeId)
      throws EmployeeNotFoundException, InvalidAttendanceException {
    findEmployeeById(employeeId);

    for (int i = attendanceRecords.size() - 1; i >= 0; i--) {
      AttendanceRecord record = attendanceRecords.get(i);
      if (Objects.equals(record.getEmployeeId(), employeeId) && record.getTimeOut() == null) {
        record.setTimeOut(LocalDateTime.now());
        return;
      }
    }

    throw new InvalidAttendanceException(
        "Employee with ID " + employeeId + " does not have an active attendance record.");
  }

  /**
   * Retrieves all completed attendance records for a given employee.
   *
   * @param employeeId the employee ID to query
   * @return list of completed attendance records
   * @throws EmployeeNotFoundException if the employee does not exist
   */
  public List<AttendanceRecord> getCompletedAttendanceRecords(String employeeId)
      throws EmployeeNotFoundException {
    findEmployeeById(employeeId);
    return attendanceRecords.stream()
        .filter(
            record ->
                Objects.equals(record.getEmployeeId(), employeeId) && record.getTimeOut() != null)
        .collect(Collectors.toList());
  }
}
