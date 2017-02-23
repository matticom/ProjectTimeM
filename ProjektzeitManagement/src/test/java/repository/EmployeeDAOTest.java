package repository;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
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

public class EmployeeDAOTest {
	
	public static final Logger log = LoggerFactory.getLogger("EmployeeDAOTest.class");

	private static IDatabaseConnection mDBUnitConnection;
	private static IDataSet startDataset;

	private static EntityManagerFactory emfactory;
	private static EntityManager entitymanager;
	private static Connection connection;

	private EmployeeDAO employeeDAO;

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

		employeeDAO = new EmployeeDAO(entitymanager);

		try {
			DatabaseOperation.CLEAN_INSERT.execute(mDBUnitConnection, startDataset);
		} catch (Exception e) {
			log.error("Exception bei DBUnit/DatabaseOperation: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testCreateAndSelectByID() {
		Employee actual = new Employee("Pilar", "Catany");
		entitymanager.getTransaction().begin();
		int id = employeeDAO.create(actual).getId();
		entitymanager.getTransaction().commit();
		entitymanager.getTransaction().begin();
		Employee expected = employeeDAO.selectById(id);
		entitymanager.getTransaction().commit();
		assertEquals(expected.getFirstName(), actual.getFirstName());
	}
	
	@Test(expected = NoResultException.class)
	public void testNoResultExceptionAtSelectById() {
		try {
			entitymanager.getTransaction().begin();
			employeeDAO.selectById(1);
			entitymanager.getTransaction().commit();
		} catch (Exception e) {
			entitymanager.getTransaction().commit();
			throw new NoResultException("Exception wurde korrekt gewurfen");
		}
	}
	
	@Test
	public void testSelectByName() {
		entitymanager.getTransaction().begin();
		List<Employee> actual = employeeDAO.selectByName("Tavo", "Siller");
		entitymanager.getTransaction().commit();
		assertEquals("Tavo", actual.get(0).getFirstName());
		assertEquals("Siller", actual.get(0).getLastName());
		assertEquals(1, actual.size());
	}
	
	@Test
	public void testUpdateEmployee() {
		entitymanager.getTransaction().begin();
		Employee tavo = employeeDAO.selectById(4);
		entitymanager.getTransaction().commit();
		employeeDAO.update(tavo, "Hugo", "Sanchez");
		entitymanager.getTransaction().begin();
		Employee hugo = employeeDAO.selectById(4);
		entitymanager.getTransaction().commit();
		assertEquals("Hugo", hugo.getFirstName());
		assertEquals("Sanchez", hugo.getLastName());
	}
	
	@Test(expected = NoResultException.class)
	public void testCreateAndDeleteEmployee() {
		
		Employee jonas = new Employee("Jonas", "Kindermann");
		entitymanager.getTransaction().begin();
		Employee jonasWithId = employeeDAO.create(jonas);
		entitymanager.getTransaction().commit();
	
		entitymanager.getTransaction().begin();
		employeeDAO.delete(jonasWithId);
		entitymanager.getTransaction().commit();
		
		try {
			entitymanager.getTransaction().begin();
			Employee shouldNotExist = employeeDAO.selectById(jonasWithId.getId());
			entitymanager.getTransaction().commit();
		} catch (NoResultException e) {
			entitymanager.getTransaction().commit();
			throw new NoResultException("Exception wurde korrekt gewurfen");
		}
	}
	
	@Test
	public void testSelectAllEmployee() {
		entitymanager.getTransaction().begin();
		List<Employee> employeeList = employeeDAO.selectAllEmployee();
		entitymanager.getTransaction().commit();
		assertEquals("Siller", employeeList.get(0).getLastName());
		assertEquals("Fournier", employeeList.get(1).getLastName());
		assertEquals(2, employeeList.size());
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
