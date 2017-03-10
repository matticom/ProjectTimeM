package businessLogic;

import java.util.List;

import javax.persistence.NoResultException;

import businessLogic.ExceptionsBL.CustomerAlreadyExisting;
import businessLogic.ExceptionsBL.CustomerDoesNotExist;
import businessLogic.interfaces.ICustomerBL;
import model.Customer;
import model.Project;
import repository.interfaces.ICustomerDAO;

public class CustomerBL implements ICustomerBL {

	private ICustomerDAO customerDAO;

	public CustomerBL(ICustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}

	public Customer createCustomer(String name) throws CustomerAlreadyExisting {
		try {
			customerDAO.selectByName(name);
			throw new CustomerAlreadyExisting(name);
		} catch (NoResultException e) {
			Customer customer = new Customer(name);
			return customerDAO.create(customer);
		}
	}

	public Customer selectCustomerByName(String name) throws CustomerDoesNotExist {
		Customer customer;
		try {
			customer = customerDAO.selectByName(name);
		} catch (NoResultException e) {
			throw new CustomerDoesNotExist(name);
		}
		return customer;
	}

	public List<Customer> selectAllCustomer() {
		List<Customer> customerList = customerDAO.selectAllCustomers();
		return customerList;
	}

	public Customer selectCustomerByID(int id) throws CustomerDoesNotExist {
		Customer customer;
		try {
			customer = customerDAO.selectById(id);
		} catch (NoResultException e) {
			throw new CustomerDoesNotExist(id);
		}
		return customer;
	}

	public Customer updateCustomer(int id, String newName) throws CustomerDoesNotExist, CustomerAlreadyExisting {
		Customer customer;
		try {
			customer = customerDAO.selectById(id);
		} catch (NoResultException e) {
			throw new CustomerDoesNotExist(id);
		}
		try {
			customerDAO.selectByName(newName);
			throw new CustomerAlreadyExisting(newName);
		} catch (NoResultException e) {
			Customer newCustomer = new Customer(newName);
			return customerDAO.update(customer, newCustomer);
		}
	}

	public void deleteCustomer(int id) throws CustomerDoesNotExist {
		Customer customer;
		try {
			customer = customerDAO.selectById(id);
		} catch (NoResultException e) {
			throw new CustomerDoesNotExist(id);
		}
		removeAllProjectsFromDeletingCustomer(customer);
		customerDAO.delete(customer);
	}

	protected void removeAllProjectsFromDeletingCustomer(Customer customer) {
		if (customer.getProjectList() == null) {
			return;
		}
		int start = customer.getProjectList().size()-1;
		for (int i = start; i >= 0; i--) {
			customer.removeProject(customer.getProjectList().get(i));
		}
	}
}
