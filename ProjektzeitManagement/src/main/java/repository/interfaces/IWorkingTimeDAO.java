package repository.interfaces;

import java.util.List;

import model.Employee;
import model.Project;
import model.WorkingTime;

public interface IWorkingTimeDAO {
	public WorkingTime create(WorkingTime workingTime);
	public WorkingTime selectById(int id);	
	public List<WorkingTime> selectStartTimeBetween(long from, long until, Project project, Employee employee);
	public List<WorkingTime> plausibilityCheckForNewTime(long newTime, Project project, Employee employee);
	public List<WorkingTime> selectAllWorkingTimes(Project project, Employee employee);
	public WorkingTime update(WorkingTime workingTime, WorkingTime newWorkingTime);
	public void delete(WorkingTime workingTime);
}
