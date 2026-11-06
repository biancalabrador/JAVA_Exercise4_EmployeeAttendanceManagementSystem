public class Employee {
  private String employeeId;
  private String name;
  private String department;

  public Employee(String employeeId, String name, String department) {
    this.employeeId = employeeId;
    this.name = name;
    this.department = department;
  }

  public String getName() {
    return name;
  }

  public String getEmployeeId() {
    return employeeId;
  }

  public String getDepartment() {
    return department;
  }
}
