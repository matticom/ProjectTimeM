package repository;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;


import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.Employee;
import model.Project;
import model.WorkingTime;

import static timestampClassConverters.TimeConvertErsatz.*;

public class WorkingTimeDAOTest {
	
	public static final Logger log = LoggerFactory.getLogger("WorkingTimeDAOTest.class");

	private static IDatabaseConnection mDBUnitConnection;
	private static IDataSet startDataset;

	private static EntityManagerFactory emfactory;
	private static EntityManager entitymanager;
	private static Connection connection;

	private WorkingTimeDAO workingTimeDAO;
	private EmployeeDAO employeeDAO;
	private ProjectDAO projectDAO;
	private long startDate;
	private long endDate;
	private Project dFBWeb;
	private Employee tavo;

	@Before
	public void setUp() {

		emfactory = Persistence.createEntityManagerFactory("Eclipselink_JPA_Derby");
		entitymanager = emfactory.createEntityManager();
		connection = ((EntityManagerImpl) (entitymanager.getDelegate())).getServerSession().getAccessor().getConnection();
		
		try {
			mDBUnitConnection = new DatabaseConnection(connection);
			mDBUnitConnection.getConfig().setProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, true);
			startDataset = new FlatXmlDataSetBuilder().build(new File("./src/test/resources/XML/testSetDAO_Start.xml"));
		} catch (Exception e) {
			log.error("Exception bei DBUnit/IDataConnection: " + e.getMessage());
			e.printStackTrace();
		}

		workingTimeDAO = new WorkingTimeDAO(entitymanager);
		employeeDAO = new EmployeeDAO(entitymanager);
		projectDAO = new ProjectDAO(entitymanager);
		startDate = instantToLong(LocalDateTime.of(2017, Month.APRIL, 1, 12, 0).toInstant(ZoneOffset.UTC));
		endDate = instantToLong(LocalDateTime.of(2018, Month.APRIL, 1, 12, 0).toInstant(ZoneOffset.UTC));
				
//		<WORKINGTIME WORKINGTIME_ID="6" WORKINGTIME_BREAKTIME_SECONDS="2700" WORKINGTIME_ENDTIME="1518281100" WORKINGTIME_STARTTIME="1518249600" EMPLOYEE_ID_FK="4" PROJECT_ID_FK="2"/>
//		<WORKINGTIME WORKINGTIME_ID="7" WORKINGTIME_BREAKTIME_SECONDS="2700" WORKINGTIME_ENDTIME="1518361100" WORKINGTIME_STARTTIME="1518329600" EMPLOYEE_ID_FK="5" PROJECT_ID_FK="2"/>
//		<WORKINGTIME WORKINGTIME_ID="8" WORKINGTIME_BREAKTIME_SECONDS="2700" WORKINGTIME_ENDTIME="1518441100" WORKINGTIME_STARTTIME="1518409600" EMPLOYEE_ID_FK="4" PROJECT_ID_FK="3"/>
//		<WORKINGTIME WORKINGTIME_ID="9" WORKINGTIME_BREAKTIME_SECONDS="2700" WORKINGTIME_ENDTIME="1518531100" WORKINGTIME_STARTTIME="1518499600" EMPLOYEE_ID_FK="5" PROJECT_ID_FK="3"/>

		try {
			DatabaseOperation.CLEAN_INSERT.execute(mDBUnitConnection, startDataset);
		} catch (Exception e) {
			log.error("Exception bei DBUnit/DatabaseOperation: " + e.getMessage());
			e.printStackTrace();
		}
		
		entitymanager.getTransaction().begin();
		tavo = employeeDAO.selectById(4);
		entitymanager.getTransaction().commit();
		entitymanager.getTransaction().begin();
		dFBWeb = projectDAO.selectById(2);
		entitymanager.getTransaction().commit();
	}
	
	@Test
	public void testSelectByID() {
		entitymanager.getTransaction().begin();
		WorkingTime actual = workingTimeDAO.selectById(6);
		entitymanager.getTransaction().commit();
		assertEquals(1518249600, actual.getStartTime());
	}

	@Test
	public void testCreateAndSelectByID() {
		WorkingTime expected = new WorkingTime(1518600000, tavo, dFBWeb);
		entitymanager.getTransaction().begin();
		int id = workingTimeDAO.create(expected).getId();
		entitymanager.getTransaction().commit();
		entitymanager.getTransaction().begin();
		WorkingTime actual = workingTimeDAO.selectById(id);
		entitymanager.getTransaction().commit();
		assertEquals(1518600000, actual.getStartTime());
		assertEquals(tavo, actual.getEmployee());
		assertEquals(dFBWeb, actual.getProject());
	}
	
	@Test(expected = NoResultException.class)
	public void testNoResultExceptionAtSelectById() {
		try {
			entitymanager.getTransaction().begin();
			workingTimeDAO.selectById(1343);
			entitymanager.getTransaction().commit();
		} catch (Exception e) {
			entitymanager.getTransaction().commit();
			throw new NoResultException("Exception wurde korrekt gewurfen");
		}
	}
	
	@Test
	public void testSelectStartTimeBetween() {
		entitymanager.getTransaction().begin();
		List<WorkingTime> actualWTList = workingTimeDAO.selectStartTimeBetween(1518249599, 1518249700, dFBWeb, tavo);
		entitymanager.getTransaction().commit();
		assertEquals(1518249600, actualWTList.get(0).getStartTime());
	}

		
	@Test
	public void testUpdateWorkingTime() {
		entitymanager.getTransaction().begin();
		WorkingTime dfbEComGuil = workingTimeDAO.selectById(9);
		entitymanager.getTransaction().commit();
		WorkingTime expectedDfbWebTavo = new WorkingTime(1518600000, tavo, dFBWeb);
		expectedDfbWebTavo.setComment("Kommentar zur Arbeitszeit");
		
		workingTimeDAO.update(dfbEComGuil, expectedDfbWebTavo);
		entitymanager.getTransaction().begin();
		WorkingTime actualDfbWebTavo = workingTimeDAO.selectById(9);
		entitymanager.getTransaction().commit();
		assertEquals(1518600000, actualDfbWebTavo.getStartTime());
	}
	
	@Test(expected = NoResultException.class)
	public void testCreateAndDeleteWorkingTime() {
		
		WorkingTime dfbWebTavo = new WorkingTime(1518600000, tavo, dFBWeb);
		entitymanager.getTransaction().begin();
		WorkingTime dfbWebTavoWithId = workingTimeDAO.create(dfbWebTavo);
		entitymanager.getTransaction().commit();
	
		entitymanager.getTransaction().begin();
		workingTimeDAO.delete(dfbWebTavoWithId);
		entitymanager.getTransaction().commit();
		
		try {
			entitymanager.getTransaction().begin();
			WorkingTime shouldNotExist = workingTimeDAO.selectById(dfbWebTavoWithId.getId());
			entitymanager.getTransaction().commit();
		} catch (NoResultException e) {
			entitymanager.getTransaction().commit();
			throw new NoResultException("Exception wurde korrekt gewurfen");
		}
	}
	
	@Test
	public void testSelectAllWorkingTimes() {
		entitymanager.getTransaction().begin();
		List<WorkingTime> workingTimeList = workingTimeDAO.selectAllWorkingTimes(dFBWeb, tavo);
		entitymanager.getTransaction().commit();
		assertEquals(1518249600, workingTimeList.get(0).getStartTime());
		assertEquals(1, workingTimeList.size());
	}

	@After
	public void tearDown() {

		try {
			IDataSet fullDataSet = mDBUnitConnection.createDataSet();
			FlatXmlDataSet.write(fullDataSet, new FileOutputStream("./src/main/resources/DatenbankContent.xml"));

		} catch (Exception e) {
			System.out.println("Es wurde eine Exception beim speichern der Datenbank geworfen: " + e.getMessage());
			e.printStackTrace();
		}
		try {
			mDBUnitConnection.close();
		} catch (SQLException e) {
			System.out.println("Es wurde eine Exception beim Schlieﬂen der IDataConnection geworfen: " + e.getMessage());
			e.printStackTrace();
		}
		entitymanager.close();
		emfactory.close();
	}
}
