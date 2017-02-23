package repository;

import javax.persistence.EntityManager;

import model.Customer;

public class CustomerDAO {
	private EntityManager entitymanager;

	public CustomerDAO(EntityManager entitymanager) {
		this.entitymanager = entitymanager;
	}
	
	public Customer create(Customer customer) {
		entitymanager.persist(customer);
		return customer;
	}
}
