package businessLogic;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import businessLogic.ExceptionsBL.ProjectDoesNotExist;
import businessLogic.ExceptionsBL.WorkingTimeDoesNotExist;
import businessLogic.RelationshipUnits.WorkingTimeRelation;
import businessLogic.interfaces.ICustomerBL;
import businessLogic.interfaces.IEmployeeBL;
import businessLogic.interfaces.IProjectBL;
import businessLogic.interfaces.IWorkingTimeBL;
import model.Project;
import model.WorkingTime;

public class RelationshipBLTest {

	private Project projectDfbWebsite;
	private List<WorkingTime> websiteWTList;
	protected IWorkingTimeBL workingTimeBL;
	@Mock protected IWorkingTimeBL workingTimeBLMock;
	@Mock protected IEmployeeBL employeeBLMock;
	@Mock protected IProjectBL projectBLMock;
	@Mock protected ICustomerBL customerBLMock;
	
		
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		FixtureFactory fixture = new FixtureFactory();
		websiteWTList = fixture.newWorkTimeListWithRelationship();
		projectDfbWebsite = fixture.newProjectWithRelationship(0);
	}
	
	@Test
	public void testRemoveWorkTimeSelectedByProject() throws ProjectDoesNotExist, WorkingTimeDoesNotExist {
		when(workingTimeBLMock.selectAllWorkingTimeByProject(4)).thenReturn(websiteWTList);
		new WorkingTimeRelation().deleteProjectRelatedWorkingTimes(projectDfbWebsite.getId(), workingTimeBLMock);
		verify(workingTimeBLMock, times(3)).deleteWorkingTime(anyInt());
	}	
}
