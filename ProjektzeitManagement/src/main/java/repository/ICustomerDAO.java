package repository;

import java.util.List;
import model.Customer;

public interface ICustomerDAO {
	public Customer create(Customer customer);
	public Customer selectById(int id);	
	public Customer selectByName(String name);	
	public List<Customer> selectAllCustomers();
	public Customer update(Customer customer, Customer newCustomer);
	public void delete(Customer customer);
}
