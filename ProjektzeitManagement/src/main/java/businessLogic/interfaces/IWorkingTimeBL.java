package businessLogic.interfaces;


import java.time.Instant;
import java.util.List;

import businessLogic.ExceptionsBL.BreakTimeException;
import businessLogic.ExceptionsBL.EmployeeDoesNotExist;
import businessLogic.ExceptionsBL.EndTimeEarlierThanStartTimeException;
import businessLogic.ExceptionsBL.ProjectDoesNotExist;
import businessLogic.ExceptionsBL.WorkingTimeAlreadyExisting;
import businessLogic.ExceptionsBL.WorkingTimeDoesNotExist;
import model.WorkingTime;

public interface IWorkingTimeBL {
	public WorkingTime createWorkingTime(Instant begin, int employeeID, int projectID) throws WorkingTimeAlreadyExisting, EmployeeDoesNotExist, ProjectDoesNotExist;

	public List<WorkingTime> selectWorkingTimeByTimeInterval(Instant from, Instant to, int employeeID, int projectID)
			throws WorkingTimeDoesNotExist, EmployeeDoesNotExist, ProjectDoesNotExist;

	public List<WorkingTime> selectAllWorkingTimeByEmployeeAndProject(int employeeID, int projectID) throws EmployeeDoesNotExist, ProjectDoesNotExist;
	public List<WorkingTime> selectAllWorkingTimeByEmployee(int employeeID) throws EmployeeDoesNotExist;
	public List<WorkingTime> selectAllWorkingTimeByProject(int projectID) throws ProjectDoesNotExist;
	public WorkingTime selectWorkingTimeByID(int id) throws WorkingTimeDoesNotExist;
	

	public WorkingTime updateWorkingTime(int id, Instant newStartTime, Instant newEndTime, int newBreakTimeSeconds, String newComment)
			throws WorkingTimeDoesNotExist, WorkingTimeAlreadyExisting, EndTimeEarlierThanStartTimeException, BreakTimeException;

	public void deleteWorkingTime(int id) throws WorkingTimeDoesNotExist;
}
