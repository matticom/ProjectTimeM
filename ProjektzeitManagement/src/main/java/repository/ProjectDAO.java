package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import model.Project;
import model.Project_;

public class ProjectDAO {
	private EntityManager entitymanager;

	public ProjectDAO(EntityManager entitymanager) {
		this.entitymanager = entitymanager;
	}

	public Project create(Project project) {
		entitymanager.persist(project);
		return project;
	}
	
	public Project selectById(int id) {
		Project project = entitymanager.find(Project.class, id);
		if (project == null) {
			throw new NoResultException("Kein Mitarbeiter mit der ID gefunden");
		}
		return project;
	}
	
//	public List<Project> selectByName(String firstName, String lastName) {
//		CriteriaBuilder criteriaBuilder = entitymanager.getCriteriaBuilder();
//		CriteriaQuery<Project> criteriaQuery = criteriaBuilder.createQuery(Project.class);
//
//		Root<Project> project = criteriaQuery.from(Project.class);
//		Predicate selectFirstName = criteriaBuilder.like(criteriaBuilder.lower(project.get(Project_.firstName)), firstName.toLowerCase());
//		Predicate selectLastName = criteriaBuilder.like(criteriaBuilder.lower(project.get(Project_.lastName)), lastName.toLowerCase());
//		Predicate whereFilter = criteriaBuilder.and(selectFirstName, selectLastName);
//		criteriaQuery.select(project).where(whereFilter);
//
//		return entitymanager.createQuery(criteriaQuery).getResultList();
//	}
//	
//	public List<Project> selectAllProject() {
//		CriteriaBuilder criteriaBuilder = entitymanager.getCriteriaBuilder();
//		CriteriaQuery<Project> criteriaQuery = criteriaBuilder.createQuery(Project.class);
//
//		Root<Project> project = criteriaQuery.from(Project.class);
//		criteriaQuery.select(project);
//		
//		return entitymanager.createQuery(criteriaQuery).getResultList();
//	}
//		
//	public Project update(Project project, String newFirstName, String newLastName) {
//		project.setFirstName(newFirstName);
//		project.setLastName(newLastName);
//		return project;
//	}
	
	public void delete(Project project) {
		entitymanager.remove(project);
	}
}
