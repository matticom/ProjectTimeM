package businessLogic.interfaces;

import java.util.List;

import businessLogic.ExceptionsBL.CustomerAlreadyExisting;
import businessLogic.ExceptionsBL.CustomerDoesNotExist;
import model.Customer;

public interface ICustomerBL {
	public Customer createCustomer(String name) throws CustomerAlreadyExisting;
	public Customer selectCustomerByName(String name) throws CustomerDoesNotExist;
	public List<Customer> selectAllCustomer();
	public Customer selectCustomerByID(int id) throws CustomerDoesNotExist;
	public Customer updateCustomer(int id, String newName) throws CustomerDoesNotExist, CustomerAlreadyExisting; 
	public void deleteCustomer(int id) throws CustomerDoesNotExist;
}
