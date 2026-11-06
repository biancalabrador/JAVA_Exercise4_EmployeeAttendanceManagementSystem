/**
 * Represents an employee registered in the attendance system.
 */
public class Employee {
  private final String employeeId;
  private final String name;
  private final String department;

  /**
   * Creates a new employee profile.
   *
   * @param employeeId the unique employee identifier
   * @param name the employee's full name
   * @param department the employee's department
   */
  public Employee(String employeeId, String name, String department) {
    this.employeeId = employeeId;
    this.name = name;
    this.department = department;
  }

  /**
   * Returns the employee's name.
   *
   * @return the employee name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the employee's unique ID.
   *
   * @return the employee ID
   */
  public String getEmployeeId() {
    return employeeId;
  }

  /**
   * Returns the employee's department.
   *
   * @return the department name
   */
  public String getDepartment() {
    return department;
  }
}
