package businessLogic;

import java.time.Instant;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import businessLogic.ExceptionsBL.CustomerAlreadyExisting;

import static timestampClassConverters.TimeConvertErsatz.*;

import model.Customer;
import repository.ICustomerDAO;

public class CustomerBL {

	private ICustomerDAO customerDAO;
	private static EntityManagerFactory emfactory;
	private static EntityManager entitymanager;
	
	public CustomerBL(ICustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
		emfactory = Persistence.createEntityManagerFactory("Eclipselink_JPA_Derby");
		entitymanager = emfactory.createEntityManager();
	}

	public Customer createCustomer(String name) throws CustomerAlreadyExisting {
//		long startDate = instantToLong(begin);
//		long endDate = instantToLong(end);
		entitymanager.getTransaction().begin();
		if (customerDAO.selectByName(name) != null) {
			throw new CustomerAlreadyExisting(name);
		}
		Customer customer = new Customer(name);
		customer = customerDAO.create(customer);
		entitymanager.getTransaction().commit();
		return customer;
	}

}
