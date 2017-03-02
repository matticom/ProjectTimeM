package repository.interfaces;

import java.util.List;
import model.Project;

public interface IProjectDAO {
	public Project create(Project project);
	public Project selectById(int id);	
	public Project selectByName(String name);	
	public List<Project> selectAllProjects();
	public Project update(Project project, Project newProject);
	public void delete(Project project);
}
