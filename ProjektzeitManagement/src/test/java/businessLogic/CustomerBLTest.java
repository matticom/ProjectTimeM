package businessLogic;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import businessLogic.ExceptionsBL.CustomerAlreadyExisting;
import businessLogic.ExceptionsBL.CustomerDoesNotExist;
import model.Customer;
import repository.interfaces.ICustomerDAO;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.persistence.NoResultException;

public class CustomerBLTest {

	@Mock protected ICustomerDAO customerDAOMock;
	protected CustomerBL customerBL;
	protected Customer expectedCustomer;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		expectedCustomer = Customer.createDbTestCustomer(1, "DFB", null);
		customerBL = new CustomerBL(customerDAOMock);
	}

	@Test
	public void testCreateCustomer() throws CustomerAlreadyExisting {
		Customer newCustomer = new Customer("DFB");
		when(customerDAOMock.create(newCustomer)).thenReturn(expectedCustomer);
		Customer dfb = customerBL.createCustomer("DFB");
		assertEquals(expectedCustomer, dfb);
		// customerDAOMock wird in DAO injiziert und ruft dann dort create auf als normaler programmablauf, das ist der Aufruf der schon zählt
		// (braucht vorher nicht extra aufgenommen und abgespielt werden wie bei Easymock
		verify(customerDAOMock).create(newCustomer); // optional um zu prüfen, dass nur einmal aufgerufen wurde
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
		Customer customer = Customer.createDbTestCustomer(1, "HSV", null);
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
		Customer customer = Customer.createDbTestCustomer(11, "HSV", null);
		when(customerDAOMock.selectById(11)).thenReturn(customer);
		when(customerDAOMock.selectByName("DFB")).thenReturn(expectedCustomer);
		customerBL.updateCustomer(11, "DFB");
	}
	
	@Test
	public void testDeleteCustomer() throws CustomerDoesNotExist {
		Customer customer = Customer.createDbTestCustomer(1, "DFB", null);
		when(customerDAOMock.selectById(1)).thenReturn(customer);
		customerBL.deleteCustomer(1);
		verify(customerDAOMock).delete(customer);
	}
	
	@Test(expected = CustomerDoesNotExist.class)
	public void testDeleteCustomerWithNotExistingId() throws CustomerDoesNotExist {
		when(customerDAOMock.selectById(11)).thenThrow(new NoResultException());
		customerBL.deleteCustomer(11);
	}
}
