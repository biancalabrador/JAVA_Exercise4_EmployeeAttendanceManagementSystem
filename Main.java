import java.time.format.DateTimeFormatter;

public class Main {
  public static void main(String[] args) {
    AttendanceManager attendanceManager = new AttendanceManager();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    try {
      Employee employee1 = new Employee("E001", "Alice", "HR");
      Employee employee2 = new Employee("E002", "Bob", "IT");
      attendanceManager.registerEmployee(employee1);
      attendanceManager.registerEmployee(employee2);

      Employee employee = attendanceManager.findEmployeeById("E001");
      System.out.println(
          "Found employee: " + employee.getName() + " (" + employee.getEmployeeId() + ")");

      attendanceManager.timeIn(employee.getEmployeeId());
      attendanceManager.timeOut(employee.getEmployeeId());

      System.out.println("Completed Attendance Records for " + employee.getEmployeeId() + ":");
      for (AttendanceRecord record :
          attendanceManager.getCompletedAttendanceRecords(employee.getEmployeeId())) {
        System.out.println(
            "Time In: " + record.getTimeIn().format(formatter)
                + ", Time Out: " + record.getTimeOut().format(formatter));
      }
    } catch (EmployeeAlreadyExistsException
        | EmployeeNotFoundException
        | InvalidAttendanceException e) {
      System.err.println(e.getMessage());
    }
  }
}
