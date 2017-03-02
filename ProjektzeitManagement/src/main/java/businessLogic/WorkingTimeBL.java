package businessLogic;

import java.time.Instant;
import java.util.List;

import javax.persistence.NoResultException;

import businessLogic.ExceptionsBL.BreakTimeException;
import businessLogic.ExceptionsBL.EmployeeDoesNotExist;
import businessLogic.ExceptionsBL.ProjectDoesNotExist;
import businessLogic.ExceptionsBL.WorkingTimeAlreadyExisting;
import businessLogic.ExceptionsBL.WorkingTimeDoesNotExist;
import businessLogic.ExceptionsBL.EndTimeEarlierThanStartTimeException;
import businessLogic.interfaces.IEmployeeBL;
import businessLogic.interfaces.IProjectBL;
import businessLogic.interfaces.IWorkingTimeBL;

import static timestampClassConverters.TimeConvertErsatz.*;

import model.Employee;
import model.Project;
import model.WorkingTime;
import repository.interfaces.IWorkingTimeDAO;

public class WorkingTimeBL implements IWorkingTimeBL{

	protected IWorkingTimeDAO workingTimeDAO;
	protected IEmployeeBL employeeBL;
	protected IProjectBL projectBL;

	public WorkingTimeBL(IWorkingTimeDAO workingTimeDAO, IEmployeeBL employeeBL, IProjectBL projectBL) {
		this.workingTimeDAO = workingTimeDAO;
		this.employeeBL = employeeBL;
		this.projectBL = projectBL;
	}

	public WorkingTime createWorkingTime(Instant begin, int employeeID, int projectID) throws WorkingTimeAlreadyExisting, EmployeeDoesNotExist, ProjectDoesNotExist {
		long startTime = instantToLong(begin);
		Employee employee = employeeBL.selectEmployeeByID(employeeID);
		Project project = projectBL.selectProjectByID(projectID);
		List<WorkingTime> workingTimeList = workingTimeDAO.plausibilityCheckForNewTime(startTime, project, employee);
		if (!workingTimeList.isEmpty()) {
			throw new WorkingTimeAlreadyExisting(begin, workingTimeList.get(0).getId());
		}
		WorkingTime workingTime = new WorkingTime(startTime, employee, project);
		return workingTimeDAO.create(workingTime);
	}

	public List<WorkingTime> selectWorkingTimeByTimeInterval(Instant from, Instant to, int employeeID, int projectID) throws WorkingTimeDoesNotExist, EmployeeDoesNotExist, ProjectDoesNotExist {
		long startTime = instantToLong(from);
		long endTime = instantToLong(to);
		Employee employee = employeeBL.selectEmployeeByID(employeeID);
		Project project = projectBL.selectProjectByID(projectID);
		List<WorkingTime> workingTimeList = workingTimeDAO.selectStartTimeBetween(startTime, endTime, project, employee);
		if (workingTimeList.isEmpty()) {
			throw new WorkingTimeDoesNotExist(from, to);
		}
		return workingTimeList;
	}

	public List<WorkingTime> selectAllWorkingTimeByEmployeeAndProject(int employeeID, int projectID) throws EmployeeDoesNotExist, ProjectDoesNotExist {
		Employee employee = employeeBL.selectEmployeeByID(employeeID);
		Project project = projectBL.selectProjectByID(projectID);
		List<WorkingTime> workingTimeList = workingTimeDAO.selectAllWorkingTimes(project, employee);
		return workingTimeList;
	}

	public WorkingTime selectWorkingTimeByID(int id) throws WorkingTimeDoesNotExist {
		WorkingTime workingTime;
		try {
			workingTime = workingTimeDAO.selectById(id);
		} catch (NoResultException e) {
			throw new WorkingTimeDoesNotExist(id);
		}
		return workingTime;
	}

	public WorkingTime updateWorkingTime(int id, Instant newStartTime, Instant newEndTime, int newBreakTimeSeconds, String newComment)
			throws WorkingTimeDoesNotExist, WorkingTimeAlreadyExisting, EndTimeEarlierThanStartTimeException, BreakTimeException {
		long start = instantToLong(newStartTime);
		long end = instantToLong(newEndTime);
		if (start >= end) {
			throw new EndTimeEarlierThanStartTimeException(newStartTime, newEndTime);
		}
		WorkingTime workingTime = selectWorkingTimeByID(id);
		plausibilityCheckForNewTimes(workingTime, id, start, end, newStartTime, newEndTime);
		if (end-start <= newBreakTimeSeconds) {
			throw new BreakTimeException(newStartTime, newEndTime, newBreakTimeSeconds);
		}
		WorkingTime newWorkingTime = new WorkingTime(start, end, newBreakTimeSeconds, newComment);
		return workingTimeDAO.update(workingTime, newWorkingTime);
	}
	
	private void plausibilityCheckForNewTimes(WorkingTime workingTime, int id, long start, long end, Instant newStartTime, Instant newEndTime) throws WorkingTimeAlreadyExisting {
		List<WorkingTime> workingTimeListStart = workingTimeDAO.plausibilityCheckForNewTime(start, workingTime.getProject(), workingTime.getEmployee());
		List<WorkingTime> workingTimeListEnd = workingTimeDAO.plausibilityCheckForNewTime(end, workingTime.getProject(), workingTime.getEmployee());
		if (!workingTimeListStart.isEmpty() && !workingTimeListEnd.isEmpty()) {
			if (workingTimeListStart.get(0).getId() != id) {
				throw new WorkingTimeAlreadyExisting(newStartTime, newEndTime, workingTimeListStart.get(0).getId());
			}
			if (workingTimeListEnd.get(0).getId() != id) {
				throw new WorkingTimeAlreadyExisting(newStartTime, newEndTime, workingTimeListEnd.get(0).getId());
			}
		}
	}

	public void deleteWorkingTime(int id) throws WorkingTimeDoesNotExist {
		WorkingTime workingTime;
		workingTime = selectWorkingTimeByID(id);
		workingTimeDAO.delete(workingTime);
	}
}
