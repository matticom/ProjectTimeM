package businessLogic;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import businessLogic.ExceptionsBL.CustomerAlreadyExisting;
import businessLogic.ExceptionsBL.CustomerDoesNotExist;
import businessLogic.ExceptionsBL.ProjectDoesNotExist;
import businessLogic.ExceptionsBL.WorkingTimeDoesNotExist;
import businessLogic.RelationshipUnits.WorkingTimeRelation;
import businessLogic.interfaces.IWorkingTimeBL;
import businessLogic.interfaces.IWorkingTimeRelation;
import model.Customer;
import model.Employee;
import model.Project;
import repository.interfaces.ICustomerDAO;
import repository.interfaces.IProjectDAO;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

public class ProjectBLTest {

	@Mock protected IProjectDAO projectDAOMock;
	@Mock protected ICustomerDAO customerDAOMock;
	@Mock protected IWorkingTimeRelation workingTimeRelationMock;
	@Mock protected IWorkingTimeBL workingTimeBL;
	protected ProjectBL projectBL;
	protected Project project;
	protected Project project2;
	protected Employee employee1;
	protected Employee employee2;
	protected Customer customer;
	

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		projectBL = new ProjectBL(projectDAOMock, customerDAOMock, workingTimeRelationMock);
		FixtureFactory fixture = new FixtureFactory();
		project = fixture.newProjectWithRelationship(0);
		customer = fixture.newCustomerWithRelationship();  // hat 2 Projekte
		employee1 = fixture.newEmloyeeWithRelationship(0); // hat 2 Projekte
		employee2 = fixture.newEmloyeeWithRelationship(1); // hat 1 Projekte
	}

	@Test
	public void testDeleteProject() throws ProjectDoesNotExist, WorkingTimeDoesNotExist {
		when(projectDAOMock.selectById(2)).thenReturn(project);
		projectBL.deleteProject(2, workingTimeBL);
		verify(projectDAOMock).delete(project);
		verify(workingTimeRelationMock).deleteProjectRelatedWorkingTimes(2, workingTimeBL);
		assertEquals(1, customer.getProjectList().size());
		assertEquals(1, employee1.getProjectList().size());
		assertEquals(0, employee2.getProjectList().size());
	}
}
