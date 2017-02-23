package repository;

import java.util.List;
import model.Employee;

public interface IEmployeeDAO {
	public Employee create(Employee employee);
	public Employee selectById(int id);	
	public List<Employee> selectByName(String firstName, String lastName);	
	public List<Employee> selectAllEmployee();
	public Employee update(Employee employee, String newFirstName, String newLastName);
	public void delete(Employee employee);
}
