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
import repository.interfaces.IProjectDAO;

public class ProjectDAO implements IProjectDAO{
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

	public List<Project> selectAllProjects() {
		CriteriaBuilder criteriaBuilder = entitymanager.getCriteriaBuilder();
		CriteriaQuery<Project> criteriaQuery = criteriaBuilder.createQuery(Project.class);

		Root<Project> project = criteriaQuery.from(Project.class);
		criteriaQuery.select(project);

		return entitymanager.createQuery(criteriaQuery).getResultList();
	}

	public Project update(Project project, Project newProject) {
		project.setName(newProject.getName());
		project.setStartDate(newProject.getStartDate());
		project.setEndDate(newProject.getEndDate());
		return project;
	}

	public void delete(Project project) {
		entitymanager.remove(project);
	}
}
