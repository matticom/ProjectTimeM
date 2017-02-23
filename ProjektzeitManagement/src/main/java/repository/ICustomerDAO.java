package repository;

import java.util.List;
import model.Customer;

public interface ICustomerDAO {
	public Customer create(Customer customer);
	public Customer selectById(int id);	
	public Customer selectByName(String name);	
	public List<Customer> selectAllCustomer();
	public Customer update(Customer customer, String newName);
	public void delete(Customer customer);
}
