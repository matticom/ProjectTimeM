package businessLogic;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import businessLogic.ExceptionsBL.EmployeeAlreadyExisting;
import model.Employee;
import repository.interfaces.IEmployeeDAO;


public class EmployeeBLTest {
	@Mock protected IEmployeeDAO employeeDAOMock;
	protected EmployeeBL employeeBL;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
//		expectedCustomer = fixture.newCustomerWithoutRelationship(1, "DFB");
		employeeBL = new EmployeeBL(employeeDAOMock);
	}
	
	@Test (expected = EmployeeAlreadyExisting.class)
	public void testCreateEmployeeWithAlreadyExistingName() throws EmployeeAlreadyExisting {
		List<Employee> daoResultList = new ArrayList<Employee>();
		daoResultList.add(new Employee());
		when(employeeDAOMock.selectByName("Vorhandener", "Name")).thenReturn(daoResultList);
		employeeBL.createEmployee("Vorhandener", "Name");
	}
	
	@Test
	public void testCreateEmployee() throws EmployeeAlreadyExisting {
		when(employeeDAOMock.selectByName("Neuer", "Name")).thenReturn(new ArrayList<Employee>());
		employeeBL.createEmployee("Neuer", "Name");
		verify(employeeDAOMock).create(new Employee("Neuer", "Name"));
	}
}
