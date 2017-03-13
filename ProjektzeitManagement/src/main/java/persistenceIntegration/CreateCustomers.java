package persistenceIntegration;

import businessLogic.CustomerBL;
import businessLogic.ExceptionsBL.CustomerAlreadyExisting;
import businessLogic.interfaces.ICustomerBL;
import model.Customer;
import repository.CustomerDAO;
// persistenceIntegration.DatabaseFunctions
public class CreateCustomers extends fit.ColumnFixture {
	private ICustomerBL customerBL;
	private DatabaseConnect dbConnect;
	private Customer customer;
	public String name; 
		
	public CreateCustomers() {
		dbConnect = DatabaseConnect.dbConnectInstance();
		customerBL = new CustomerBL(new CustomerDAO(dbConnect.getEntitymanager()));
	}

	public String createNewCustomerAndGetName() throws CustomerAlreadyExisting {
		dbConnect.getEntitymanager().getTransaction().begin();
		try {
			customer = customerBL.createCustomer(name);
		} catch (Exception e) {
			dbConnect.getEntitymanager().getTransaction().commit();
			return e.getMessage();
		}
		dbConnect.getEntitymanager().getTransaction().commit();
		return customer.getName();
	}
	
	public int getNewCustomerId() {
		dbConnect.createDbImage();
		if (customer == null) {
			return 0;
		}
		return customer.getId(); 
	}

}
