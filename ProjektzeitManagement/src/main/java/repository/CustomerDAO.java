package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import model.Customer;
import model.Customer_;
import repository.interfaces.ICustomerDAO;

public class CustomerDAO implements ICustomerDAO {
	private EntityManager entitymanager;

	public CustomerDAO(EntityManager entitymanager) {
		this.entitymanager = entitymanager;
	}

	public Customer create(Customer customer) {
		entitymanager.persist(customer);
		return customer;
	}

	public Customer selectById(int id) {
		Customer customer = entitymanager.find(Customer.class, id);
		if (customer == null) {
			throw new NoResultException("Kein Kunde mit der ID gefunden");
		}
		return customer;
	}

	public Customer selectByName(String name) throws NoResultException {
		CriteriaBuilder criteriaBuilder = entitymanager.getCriteriaBuilder();
		CriteriaQuery<Customer> criteriaQuery = criteriaBuilder.createQuery(Customer.class);

		Root<Customer> customer = criteriaQuery.from(Customer.class);
		Predicate whereFilter = criteriaBuilder.like(criteriaBuilder.lower(customer.get(Customer_.name)), name.toLowerCase());
		criteriaQuery.select(customer).where(whereFilter);

		return entitymanager.createQuery(criteriaQuery).getSingleResult();
	}

	public List<Customer> selectAllCustomers() {
		CriteriaBuilder criteriaBuilder = entitymanager.getCriteriaBuilder();
		CriteriaQuery<Customer> criteriaQuery = criteriaBuilder.createQuery(Customer.class);

		Root<Customer> customer = criteriaQuery.from(Customer.class);
		criteriaQuery.select(customer);

		return entitymanager.createQuery(criteriaQuery).getResultList();
	}

	public Customer update(Customer customer, Customer newCustomer) {
		customer.setName(newCustomer.getName());
		return customer;
	}

	public void delete(Customer customer) {
		entitymanager.remove(customer);
	}
}
