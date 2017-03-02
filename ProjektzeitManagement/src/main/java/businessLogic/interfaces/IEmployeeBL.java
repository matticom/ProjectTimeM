package businessLogic.interfaces;

import java.util.List;

import businessLogic.ExceptionsBL.EmployeeAlreadyExisting;
import businessLogic.ExceptionsBL.EmployeeDoesNotExist;
import model.Employee;

public interface IEmployeeBL {
	public Employee createEmployee(String firstname, String lastname) throws EmployeeAlreadyExisting;
	public List<Employee> selectEmployeeByName(String firstname, String lastname) throws EmployeeDoesNotExist;
	public List<Employee> selectAllEmployee();
	public Employee selectEmployeeByID(int id) throws EmployeeDoesNotExist;
	public Employee updateEmployee(int id, String firstname, String lastname) throws EmployeeDoesNotExist, EmployeeAlreadyExisting;
	public void deleteEmployee(int id) throws EmployeeDoesNotExist;
}
