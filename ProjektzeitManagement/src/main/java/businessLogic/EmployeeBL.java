package businessLogic;

import java.util.List;

import javax.persistence.NoResultException;

import businessLogic.ExceptionsBL.EmployeeAlreadyExisting;
import businessLogic.ExceptionsBL.EmployeeDoesNotExist;
import businessLogic.interfaces.IEmployeeBL;
import model.Employee;
import repository.interfaces.IEmployeeDAO;

public class EmployeeBL implements IEmployeeBL {

	private IEmployeeDAO employeeDAO;

	public EmployeeBL(IEmployeeDAO employeeDAO) {
		this.employeeDAO = employeeDAO;
	}

	public Employee createEmployee(String firstname, String lastname) throws EmployeeAlreadyExisting {
		if (!employeeDAO.selectByName(firstname, lastname).isEmpty()) {
			throw new EmployeeAlreadyExisting(firstname, lastname);
		}
		Employee employee = new Employee(firstname, lastname);
		return employeeDAO.create(employee);
	}

	public List<Employee> selectEmployeeByName(String firstname, String lastname) throws EmployeeDoesNotExist {
		List<Employee> employeeList = employeeDAO.selectByName(firstname, lastname);
		if (employeeList.isEmpty()) {
			throw new EmployeeDoesNotExist(firstname, lastname);
		}
		return employeeList;
	}

	public List<Employee> selectAllEmployee() {
		List<Employee> employeeList = employeeDAO.selectAllEmployees();
		return employeeList;
	}

	public Employee selectEmployeeByID(int id) throws EmployeeDoesNotExist {
		Employee employee;
		try {
			employee = employeeDAO.selectById(id);
		} catch (NoResultException e) {
			throw new EmployeeDoesNotExist(id);
		}
		return employee;
	}

	public Employee updateEmployee(int id, String firstname, String lastname) throws EmployeeDoesNotExist, EmployeeAlreadyExisting {
		Employee employee = selectEmployeeByID(id);
		List<Employee> employeeList = employeeDAO.selectByName(firstname, lastname);
		if (employeeList.isEmpty()) {
			Employee newEmployee = new Employee(firstname, lastname);
			return employeeDAO.update(employee, newEmployee);
		} else {
			throw new EmployeeAlreadyExisting(firstname, lastname);
		}
	}

	public void deleteEmployee(int id) throws EmployeeDoesNotExist {
		Employee employee = selectEmployeeByID(id);
		employeeDAO.delete(employee);
	}
}
