package businessLogic;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import businessLogic.ExceptionsBL.CustomerAlreadyExisting;
import model.Customer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import repository.ICustomerDAO;

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
	
	
}
