package businessLogic;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import businessLogic.ExceptionsBL.CustomerAlreadyExisting;
import businessLogic.ExceptionsBL.CustomerDoesNotExist;
import model.Customer;
import model.Project;
import repository.interfaces.ICustomerDAO;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

public class CustomerBLTest {

	@Mock protected ICustomerDAO customerDAOMock;
	protected CustomerBL customerBL;
	protected Customer expectedCustomer;
	protected FixtureFactory fixture;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		fixture = new FixtureFactory();
		expectedCustomer = fixture.newCustomerWithoutRelationship(1, "DFB");
		customerBL = new CustomerBL(customerDAOMock);
	}

	@Test
	public void testCreateCustomer() throws CustomerAlreadyExisting {
		Customer newCustomer = new Customer("DFB");
		when(customerDAOMock.selectByName("DFB")).thenThrow(new NoResultException());
		when(customerDAOMock.create(newCustomer)).thenReturn(expectedCustomer);
		Customer dfb = customerBL.createCustomer("DFB");
		assertEquals(expectedCustomer, dfb);
		verify(customerDAOMock).create(newCustomer);
	}

	@Test(expected = CustomerAlreadyExisting.class)
	public void testCreateCustomerWithAlreadyExistingName() throws CustomerAlreadyExisting {
		when(customerDAOMock.selectByName("DFB")).thenReturn(expectedCustomer);
		Customer dfb = customerBL.createCustomer("DFB");
	}
	
	@Test
	public void testSelectCustomerByName() throws CustomerDoesNotExist {
		when(customerDAOMock.selectByName("DFB")).thenReturn(expectedCustomer);
		Customer dfb = customerBL.selectCustomerByName("DFB");
		assertEquals(expectedCustomer, dfb);
	}
	
	@Test(expected = CustomerDoesNotExist.class)
	public void testSelectCustomerByNameWithUnkownName() throws CustomerDoesNotExist {
		when(customerDAOMock.selectByName("HSV")).thenThrow(new NoResultException());
		customerBL.selectCustomerByName("HSV");
	}
	
	@Test(expected = CustomerDoesNotExist.class)
	public void testSelectCustomerByIdWithNoResultException() throws CustomerDoesNotExist {
		when(customerDAOMock.selectById(99)).thenThrow(new NoResultException());
		customerBL.selectCustomerByID(99);
	}
	
	@Test
	public void testUpdateCustomer() throws CustomerDoesNotExist, CustomerAlreadyExisting {
		Customer customer = fixture.newCustomerWithoutRelationship(1, "HSV");
		Customer newCustomer = new Customer("DFB");
		when(customerDAOMock.selectById(1)).thenReturn(customer);
		when(customerDAOMock.selectByName("DFB")).thenThrow(new NoResultException());
		when(customerDAOMock.update(customer, newCustomer)).thenReturn(expectedCustomer);
		Customer dfb = customerBL.updateCustomer(1, "DFB");
		assertEquals(expectedCustomer, dfb);
	}
	
	@Test(expected = CustomerDoesNotExist.class)
	public void testUpdateCustomerWithNotExistingId() throws CustomerDoesNotExist, CustomerAlreadyExisting {
		when(customerDAOMock.selectById(1)).thenThrow(new NoResultException());
		customerBL.updateCustomer(1, "DFB");
	}
	
	@Test(expected = CustomerAlreadyExisting.class)
	public void testUpdateCustomerWithAlreadyExistingNewCustomer() throws CustomerDoesNotExist, CustomerAlreadyExisting {
		Customer customer = fixture.newCustomerWithoutRelationship(11, "HSV");
		when(customerDAOMock.selectById(11)).thenReturn(customer);
		when(customerDAOMock.selectByName("DFB")).thenReturn(expectedCustomer);
		customerBL.updateCustomer(11, "DFB");
	}
	
	@Test
	public void testDeleteCustomer() throws CustomerDoesNotExist {
		Customer customer = fixture.newCustomerWithoutRelationship(1, "DFB");
		when(customerDAOMock.selectById(1)).thenReturn(customer);
		customerBL.deleteCustomer(1);
		verify(customerDAOMock).delete(customer);
	}
	
	@Test(expected = CustomerDoesNotExist.class)
	public void testDeleteCustomerWithNotExistingId() throws CustomerDoesNotExist {
		when(customerDAOMock.selectById(11)).thenThrow(new NoResultException());
		customerBL.deleteCustomer(11);
	}
	
	@Test
	public void testDeleteCustomerWithProjectRemoving() throws CustomerDoesNotExist {
		Project project1 = fixture.newProjectWithRelationship(0);
		Project project2 = fixture.newProjectWithRelationship(1);
		Customer customer = fixture.newCustomerWithRelationship();
		when(customerDAOMock.selectById(1)).thenReturn(customer);
		customerBL.deleteCustomer(1);
		verify(customerDAOMock).delete(customer);
		assertEquals(null, project1.getCustomer());
		assertEquals(null, project2.getCustomer());
	}
}
