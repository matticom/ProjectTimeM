package businessLogic;

import java.time.Instant;
import java.util.List;

import javax.persistence.NoResultException;

import businessLogic.ExceptionsBL.ProjectAlreadyExisting;
import businessLogic.ExceptionsBL.ProjectDoesNotExist;
import businessLogic.interfaces.IProjectBL;

import static timestampClassConverters.TimeConvertErsatz.*;

import model.Project;
import repository.interfaces.IProjectDAO;

public class ProjectBL implements IProjectBL {

	private IProjectDAO projectDAO;

	public ProjectBL(IProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public Project createProject(String name, Instant begin, Instant end) throws ProjectAlreadyExisting {
		long startDate = instantToLong(begin);
		long endDate = instantToLong(end);
		if (projectDAO.selectByName(name) != null) {
			throw new ProjectAlreadyExisting(name);
		}
		Project project = new Project(name, startDate, endDate);
		return projectDAO.create(project);
	}

	public Project selectProjectByName(String name) throws ProjectDoesNotExist {
		Project project;
		try {
			project = projectDAO.selectByName(name);
		} catch (NoResultException e) {
			throw new ProjectDoesNotExist(name);
		}
		return project;
	}

	public List<Project> selectAllProject() {
		List<Project> projectList = projectDAO.selectAllProjects();
		return projectList;
	}

	public Project selectProjectByID(int id) throws ProjectDoesNotExist {
		Project project;
		try {
			project = projectDAO.selectById(id);
		} catch (NoResultException e) {
			throw new ProjectDoesNotExist(id);
		}
		return project;
	}

	public Project updateProject(int id, String newName, Instant newBegin, Instant newEnd) throws ProjectDoesNotExist, ProjectAlreadyExisting {
		Project project;
		try {
			project = projectDAO.selectById(id);
		} catch (NoResultException e) {
			throw new ProjectDoesNotExist(id);
		}
		try {
			projectDAO.selectByName(newName);
			throw new ProjectAlreadyExisting(newName);
		} catch (NoResultException e) {
			long startDate = instantToLong(newBegin);
			long endDate = instantToLong(newEnd);
			Project newProject = new Project(newName, startDate, endDate);
			return projectDAO.update(project, newProject);
		}
	}

	public void deleteProject(int id) throws ProjectDoesNotExist {
		Project project;
		try {
			project = projectDAO.selectById(id);
		} catch (NoResultException e) {
			throw new ProjectDoesNotExist(id);
		}
		projectDAO.delete(project);
	}
}
