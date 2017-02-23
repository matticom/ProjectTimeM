package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import model.Employee;
import model.Employee_;

public class EmployeeDAO implements IEmployeeDAO {
	private EntityManager entitymanager;

	public EmployeeDAO(EntityManager entitymanager) {
		this.entitymanager = entitymanager;
	}

	public Employee create(Employee employee) {
		entitymanager.persist(employee);
		return employee;
	}
	
	public Employee selectById(int id) {
		Employee employee = entitymanager.find(Employee.class, id);
		if (employee == null) {
			throw new NoResultException("Kein Mitarbeiter mit der ID gefunden");
		}
		return employee;
	}
	
	public List<Employee> selectByName(String firstName, String lastName) {
		CriteriaBuilder criteriaBuilder = entitymanager.getCriteriaBuilder();
		CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);

		Root<Employee> employee = criteriaQuery.from(Employee.class);
		Predicate selectFirstName = criteriaBuilder.like(criteriaBuilder.lower(employee.get(Employee_.firstName)), firstName.toLowerCase());
		Predicate selectLastName = criteriaBuilder.like(criteriaBuilder.lower(employee.get(Employee_.lastName)), lastName.toLowerCase());
		Predicate whereFilter = criteriaBuilder.and(selectFirstName, selectLastName);
		criteriaQuery.select(employee).where(whereFilter);

		return entitymanager.createQuery(criteriaQuery).getResultList();
	}
	
	public List<Employee> selectAllEmployee() {
		CriteriaBuilder criteriaBuilder = entitymanager.getCriteriaBuilder();
		CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);

		Root<Employee> employee = criteriaQuery.from(Employee.class);
		criteriaQuery.select(employee);
		
		return entitymanager.createQuery(criteriaQuery).getResultList();
	}
		
	public Employee update(Employee employee, String newFirstName, String newLastName) {
		employee.setFirstName(newFirstName);
		employee.setLastName(newLastName);
		return employee;
	}
	
	public void delete(Employee employee) {
		entitymanager.remove(employee);
	}
}
