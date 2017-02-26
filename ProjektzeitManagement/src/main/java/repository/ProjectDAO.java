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
			throw new NoResultException("Kein Projekt mit der ID gefunden");
		}
		return project;
	}
	
	public Project selectByName(String name) {
		CriteriaBuilder criteriaBuilder = entitymanager.getCriteriaBuilder();
		CriteriaQuery<Project> criteriaQuery = criteriaBuilder.createQuery(Project.class);

		Root<Project> project = criteriaQuery.from(Project.class);
		Predicate whereFilter = criteriaBuilder.like(criteriaBuilder.lower(project.get(Project_.name)), name.toLowerCase());
		criteriaQuery.select(project).where(whereFilter);

		return entitymanager.createQuery(criteriaQuery).getSingleResult();
	}
	
	public List<Project> selectAllProject() {
		CriteriaBuilder criteriaBuilder = entitymanager.getCriteriaBuilder();
		CriteriaQuery<Project> criteriaQuery = criteriaBuilder.createQuery(Project.class);

		Root<Project> project = criteriaQuery.from(Project.class);
		criteriaQuery.select(project);
		
		return entitymanager.createQuery(criteriaQuery).getResultList();
	}
		
	public Project update(Project project, String newName) {
		project.setName(newName);
		return project;
	}
	
	public void delete(Project project) {
		entitymanager.remove(project);
	}
}
