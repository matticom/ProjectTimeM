package businessLogic;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import businessLogic.ExceptionsBL.EmployeeDoesNotExist;
import businessLogic.ExceptionsBL.ProjectDoesNotExist;
import businessLogic.ExceptionsBL.WorkingTimeAlreadyExisting;
import businessLogic.ExceptionsBL.WorkingTimeDoesNotExist;
import businessLogic.interfaces.IEmployeeBL;
import businessLogic.interfaces.IProjectBL;
import model.Customer;
import model.Employee;
import model.Project;
import model.WorkingTime;
import repository.interfaces.IWorkingTimeDAO;

import static timestampClassConverters.TimeConvertErsatz.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

public class WorkingTimeBLTest {
	
	@Mock protected IWorkingTimeDAO workingTimeDAOMock;
	@Mock protected IEmployeeBL employeeBLMock;
	@Mock protected IProjectBL projectBLMock;
	protected WorkingTimeBL workingTimeBL;
	protected Project byMockCreatedProject;
	protected Employee byMockCreatedEmployee;
	protected WorkingTime expectedWorkingTime;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		byMockCreatedEmployee = new Employee("Tavo", "Siller");
		byMockCreatedProject = new Project("DFB Webseite", 1486731600, 1518267600, new Customer());
		when(employeeBLMock.selectEmployeeByID(4)).thenReturn(byMockCreatedEmployee);
		when(projectBLMock.selectProjectByID(2)).thenReturn(byMockCreatedProject);
		
		workingTimeBL = new WorkingTimeBL(workingTimeDAOMock, employeeBLMock, projectBLMock);
		expectedWorkingTime = new WorkingTime(1486731600l, byMockCreatedEmployee, byMockCreatedProject);
	}

	@Test
	public void testCreateWorkingTime() throws WorkingTimeAlreadyExisting, EmployeeDoesNotExist, ProjectDoesNotExist {		
		when(workingTimeDAOMock.plausibilityCheckForNewTime(1486731600, byMockCreatedProject, byMockCreatedEmployee)).thenReturn(new ArrayList<WorkingTime>());
		workingTimeBL.createWorkingTime(longToInstant(1486731600l), 4, 2);
		assertEquals(true, byMockCreatedEmployee.getWorkingTimeList().contains(expectedWorkingTime));
		assertEquals(true, byMockCreatedProject.getWorkingTimeList().contains(expectedWorkingTime));
		assertEquals(true, byMockCreatedProject.getEmployeeList().contains(byMockCreatedEmployee));
		assertEquals(true, byMockCreatedEmployee.getProjectList().contains(byMockCreatedProject));
		verify(workingTimeDAOMock).create(expectedWorkingTime);
	}
	
	@Test
	public void testCreateWorkingTimeWithAlreadyRelatedEmployeeAndProject() throws WorkingTimeAlreadyExisting, EmployeeDoesNotExist, ProjectDoesNotExist {		
		when(workingTimeDAOMock.plausibilityCheckForNewTime(1486731600, byMockCreatedProject, byMockCreatedEmployee)).thenReturn(new ArrayList<WorkingTime>());
		byMockCreatedEmployee.addProject(byMockCreatedProject);
		workingTimeBL.createWorkingTime(longToInstant(1486731600l), 4, 2);
		assertEquals(1, byMockCreatedProject.getEmployeeList().size());
		assertEquals(1, byMockCreatedEmployee.getProjectList().size());
		verify(workingTimeDAOMock).create(expectedWorkingTime);
	}
	
	@Test (expected = WorkingTimeAlreadyExisting.class)
	public void testCreateWorkingTimeWithOverlappingStartTime() throws WorkingTimeAlreadyExisting, EmployeeDoesNotExist, ProjectDoesNotExist {
		WorkingTime overlappedWorkingTime = new WorkingTime(1486731600l, byMockCreatedEmployee, byMockCreatedProject);
		List<WorkingTime> overlappingWorkTimeList = new ArrayList<WorkingTime>();
		overlappingWorkTimeList.add(overlappedWorkingTime);
		when(workingTimeDAOMock.plausibilityCheckForNewTime(1486731600, byMockCreatedProject, byMockCreatedEmployee)).thenReturn(overlappingWorkTimeList);
		workingTimeBL.createWorkingTime(longToInstant(1486731600l), 4, 2);
	}
	
	@Test
	public void testSelectWorkingTimeByTimeInterval() throws Exception {
		List<WorkingTime> expectedWorkTimeList = prepareExpectedWorkTimeList(0);
		when(workingTimeDAOMock.selectStartTimeBetween(1486731400l, 1486731800l, byMockCreatedProject, byMockCreatedEmployee)).thenReturn(expectedWorkTimeList);
		List<WorkingTime> actualWorkTimeList = workingTimeBL.selectWorkingTimeByTimeInterval(longToInstant(1486731400l), longToInstant(1486731800l), 4, 2);
		assertEquals(expectedWorkTimeList, actualWorkTimeList);
	}
	
	@Test (expected = WorkingTimeDoesNotExist.class)
	public void testSelectWorkingTimeByTimeIntervalWithNoResult() throws Exception {
		when(workingTimeDAOMock.selectStartTimeBetween(1486731400l, 1486731800l, byMockCreatedProject, byMockCreatedEmployee)).thenReturn(new ArrayList<WorkingTime>());
		workingTimeBL.selectWorkingTimeByTimeInterval(longToInstant(1486731100l), longToInstant(1486731200l), 4, 2);
	}
	
	@Test (expected = WorkingTimeDoesNotExist.class)
	public void testSelectWorkingTimeByIDwithNoExistingWorkingTime() throws Exception {
		when(workingTimeDAOMock.selectById(1)).thenThrow(new NoResultException());
		workingTimeBL.selectWorkingTimeByID(1);
	}
	
	@Test
	public void testUpdateWorkingTime() throws Exception {	
		WorkingTime selectedWorkingTime = prepareSelectedWorkingTime();
		when(workingTimeDAOMock.selectById(6)).thenReturn(selectedWorkingTime);
		when(workingTimeDAOMock.plausibilityCheckForNewTime(1486731400l, byMockCreatedProject, byMockCreatedEmployee)).thenReturn(new ArrayList<WorkingTime>());
		when(workingTimeDAOMock.plausibilityCheckForNewTime(1486731800l, byMockCreatedProject, byMockCreatedEmployee)).thenReturn(new ArrayList<WorkingTime>());
		expectedWorkingTime = new WorkingTime(1486731400l, 1486761800l, 2700, "Testzeit");
		workingTimeBL.updateWorkingTime(6, longToInstant(1486731400l), longToInstant(1486761800l), 2700, "Testzeit");
		verify(workingTimeDAOMock).update(selectedWorkingTime, expectedWorkingTime);
	}
	
	@Test (expected = WorkingTimeAlreadyExisting.class)
	public void testUpdateWorkingTimeWithTimeConflictsOfAlreadyExistingWorkingTimes() throws Exception {	
		WorkingTime selectedWorkingTime = prepareSelectedWorkingTime();
		when(workingTimeDAOMock.selectById(6)).thenReturn(selectedWorkingTime);
		List<WorkingTime> expectedWorkTimeList = prepareExpectedWorkTimeList(7);
		when(workingTimeDAOMock.plausibilityCheckForNewTime(1486731400l, byMockCreatedProject, byMockCreatedEmployee)).thenReturn(new ArrayList<WorkingTime>());
		when(workingTimeDAOMock.plausibilityCheckForNewTime(1486761800l, byMockCreatedProject, byMockCreatedEmployee)).thenReturn(expectedWorkTimeList);
		workingTimeBL.updateWorkingTime(6, longToInstant(1486731400l), longToInstant(1486761800l), 2700, "Testzeit");
	}
		
	@Test
	public void testDeleteWorkingTime() throws Exception {	
		byMockCreatedEmployee.addWorkingTime(expectedWorkingTime);
		byMockCreatedProject.addWorkingTime(expectedWorkingTime);
		when(workingTimeDAOMock.selectById(6)).thenReturn(expectedWorkingTime);
		workingTimeBL.deleteWorkingTime(6);
		verify(workingTimeDAOMock).delete(expectedWorkingTime);
		assertEquals(byMockCreatedEmployee.getWorkingTimeList().size(), 0);
		assertEquals(byMockCreatedProject.getWorkingTimeList().size(), 0);
	}
	
	private WorkingTime prepareSelectedWorkingTime() {
		WorkingTime selectedWorkingTime = new WorkingTime(1486731600l, byMockCreatedEmployee, byMockCreatedProject);
		selectedWorkingTime.setEmployee(byMockCreatedEmployee);
		selectedWorkingTime.setProject(byMockCreatedProject);
		selectedWorkingTime.setId(6);
		return selectedWorkingTime;
	}
	
	private List<WorkingTime> prepareExpectedWorkTimeList(int idOfContainedWorkingTime) {
		WorkingTime foundWorkingTime = new WorkingTime();
		foundWorkingTime.setId(idOfContainedWorkingTime);
		List<WorkingTime> expectedWorkTimeList = new ArrayList<WorkingTime>();
		expectedWorkTimeList.add(foundWorkingTime);
		return expectedWorkTimeList;
	}
}
