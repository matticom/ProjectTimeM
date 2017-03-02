package repository.interfaces;

import java.util.List;
import model.Employee;

public interface IEmployeeDAO {
	public Employee create(Employee employee);
	public Employee selectById(int id);	
	public List<Employee> selectByName(String firstName, String lastName);	
	public List<Employee> selectAllEmployees();
	public Employee update(Employee employee, Employee newEmployee);
	public void delete(Employee employee);
}
