package businessLogic;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

import businessLogic.ExceptionsBL.CustomerDoesNotExist;
import businessLogic.ExceptionsBL.ProjectAlreadyExisting;
import businessLogic.ExceptionsBL.ProjectDoesNotExist;
import businessLogic.ExceptionsBL.WorkingTimeDoesNotExist;
import businessLogic.RelationshipUnits.WorkingTimeRelation;
import businessLogic.interfaces.IProjectBL;
import businessLogic.interfaces.IWorkingTimeBL;
import businessLogic.interfaces.IWorkingTimeRelation;

import static timestampClassConverters.TimeConvertErsatz.*;

import model.Customer;
import model.Employee;
import model.Project;
import repository.interfaces.ICustomerDAO;
import repository.interfaces.IProjectDAO;

public class ProjectBL implements IProjectBL {

	private IProjectDAO projectDAO;
	private ICustomerDAO customerDAO;
	private IWorkingTimeRelation workingTimeRelation;

	public ProjectBL(IProjectDAO projectDAO, ICustomerDAO customerDAO, IWorkingTimeRelation workingTimeRelation) {
		this.projectDAO = projectDAO;
		this.customerDAO = customerDAO;
		this.workingTimeRelation = workingTimeRelation;
	}

	public Project createProject(String name, Instant begin, Instant end, int customerId) throws ProjectAlreadyExisting, CustomerDoesNotExist {
		long startDate = instantToLong(begin);
		long endDate = instantToLong(end);
		Customer customer;
		try {
			customer = customerDAO.selectById(customerId);
		} catch (NoResultException e) {
			throw new CustomerDoesNotExist(customerId);
		}
		try {
			projectDAO.selectByName(name);
			throw new ProjectAlreadyExisting(name);
		} catch (NoResultException e) {
			Project project = new Project(name, startDate, endDate, customer);
			return projectDAO.create(project);
		}		
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
		return selectProjectById(id);
	}

	public Project updateProject(int id, String newName, Instant newBegin, Instant newEnd) throws ProjectDoesNotExist, ProjectAlreadyExisting {
		Project project = selectProjectById(id);
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

	public void deleteProject(int id, IWorkingTimeBL workingTimeBL) throws ProjectDoesNotExist, WorkingTimeDoesNotExist {
		workingTimeRelation.deleteProjectRelatedWorkingTimes(id, workingTimeBL);
		Project project = selectProjectById(id);
		removeAllEmployeesFromProject(project);
		project.getCustomer().getProjectList().remove(project);
		project.setCustomer(null);
		projectDAO.delete(project);
	}
	
	private void removeAllEmployeesFromProject(Project project) {
		List<Employee> employeeList = new ArrayList<Employee>(project.getEmployeeList());
		for (Employee employee : employeeList) {
			project.removeEmployee(employee);
		}
	}
	
	private Project selectProjectById(int id) throws ProjectDoesNotExist {
		Project project;
		try {
			project = projectDAO.selectById(id);
		} catch (NoResultException e) {
			throw new ProjectDoesNotExist(id);
		}
		return project;
	}

}
