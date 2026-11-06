import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AttendanceManager {
  private List<Employee> employees;
  private List<AttendanceRecord> attendanceRecords;

  public AttendanceManager() {
    employees = new ArrayList<>();
    attendanceRecords = new ArrayList<>();
  }

  public void registerEmployee(Employee employee) throws EmployeeAlreadyExistsException {
    for (Employee registeredEmployee : employees) {
      if (Objects.equals(registeredEmployee.getEmployeeId(), employee.getEmployeeId())) {
        throw new EmployeeAlreadyExistsException(
            "Employee with ID " + employee.getEmployeeId() + " already exists.");
      }
    }
    employees.add(employee);
  }

  public Employee findEmployeeById(String employeeId) throws EmployeeNotFoundException {
    for (Employee employee : employees) {
      if (Objects.equals(employee.getEmployeeId(), employeeId)) {
        return employee;
      }
    }
    throw new EmployeeNotFoundException("Employee with ID " + employeeId + " was not found.");
  }

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

  public List<AttendanceRecord> getCompletedAttendanceRecords(String employeeId)
      throws EmployeeNotFoundException {
    findEmployeeById(employeeId);
    return attendanceRecords.stream()
        .filter(record ->
            Objects.equals(record.getEmployeeId(), employeeId) && record.getTimeOut() != null)
        .collect(Collectors.toList());
  }
}
