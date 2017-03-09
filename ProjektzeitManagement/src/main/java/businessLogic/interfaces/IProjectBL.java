package businessLogic.interfaces;

import java.time.Instant;
import java.util.List;

import businessLogic.ExceptionsBL.CustomerDoesNotExist;
import businessLogic.ExceptionsBL.ProjectAlreadyExisting;
import businessLogic.ExceptionsBL.ProjectDoesNotExist;
import model.Project;

public interface IProjectBL {
	public Project createProject(String name, Instant begin, Instant end, int customerId) throws ProjectAlreadyExisting, CustomerDoesNotExist;
	public Project selectProjectByName(String name) throws ProjectDoesNotExist;
	public List<Project> selectAllProject();
	public Project selectProjectByID(int id) throws ProjectDoesNotExist;
	public Project updateProject(int id, String newName, Instant newBegin, Instant newEnd) throws ProjectDoesNotExist, ProjectAlreadyExisting;
	public void deleteProject(int id) throws ProjectDoesNotExist;
}
