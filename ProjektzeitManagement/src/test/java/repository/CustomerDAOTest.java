package repository;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import org.dbunit.dataset.DataSetException;
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

import model.Customer;

public class CustomerDAOTest {
	
	public static final Logger log = LoggerFactory.getLogger("CustomerDAOTest.class");

	private static IDatabaseConnection mDBUnitConnection;
	private static IDataSet startDataset;

	private static EntityManagerFactory emfactory;
	private static EntityManager entitymanager;
	private static Connection connection;

	private CustomerDAO customerDAO;

	@Before
	public void setUp() {

		try {
			emfactory = Persistence.createEntityManagerFactory("Eclipselink_JPA_Derby");
			entitymanager = emfactory.createEntityManager();
			connection = ((EntityManagerImpl) (entitymanager.getDelegate())).getServerSession().getAccessor().getConnection();
		} catch (Exception e) {
			log.error("Exception bei EntityManager/Connection geworfen: " + e.getMessage());
			e.printStackTrace();
		}
		
		try {
			mDBUnitConnection = new DatabaseConnection(connection);
			mDBUnitConnection.getConfig().setProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, true);
			startDataset = new FlatXmlDataSetBuilder().build(new File("./src/test/resources/XML/testSetDAO_Start.xml"));
		} catch (Exception e) {
			log.error("Exception bei DBUnit/IDataConnection: " + e.getMessage());
			e.printStackTrace();
		}

		customerDAO = new CustomerDAO(entitymanager);

		try {
			DatabaseOperation.CLEAN_INSERT.execute(mDBUnitConnection, startDataset);
		} catch (Exception e) {
			log.error("Exception bei DBUnit/DatabaseOperation: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testSelectByID() {
		entitymanager.getTransaction().begin();
		Customer actual = customerDAO.selectById(1);
		entitymanager.getTransaction().commit();
		assertEquals("DFB", actual.getName());
	}
	
	@Test
	public void testCreateAndSelectByID() {
		Customer actual = new Customer("Monster INC");
		entitymanager.getTransaction().begin();
		int id = customerDAO.create(actual).getId();
		entitymanager.getTransaction().commit();
		entitymanager.getTransaction().begin();
		Customer expected = customerDAO.selectById(id);
		entitymanager.getTransaction().commit();
		assertEquals(expected.getName(), actual.getName());
	}
		
	@Test(expected = NoResultException.class)
	public void testNoResultExceptionAtSelectById() {
		try {
			entitymanager.getTransaction().begin();
			customerDAO.selectById(18);
			entitymanager.getTransaction().commit();
		} catch (Exception e) {
			entitymanager.getTransaction().commit();
			throw new NoResultException("Exception wurde korrekt gewurfen");
		}
	}
	
	@Test
	public void testSelectByName() {
		entitymanager.getTransaction().begin();
		Customer actual = customerDAO.selectByName("DFB");
		entitymanager.getTransaction().commit();
		assertEquals("DFB", actual.getName());
	}
	
	@Test(expected = NoResultException.class)
	public void testNoResultExceptionAtSelectByName() {
		try {
			entitymanager.getTransaction().begin();
			customerDAO.selectByName("Monster INC");
			entitymanager.getTransaction().commit();
		} catch (Exception e) {
			entitymanager.getTransaction().commit();
			throw new NoResultException("Exception wurde korrekt gewurfen");
		}
	}
		
	@Test
	public void testUpdateCustomer() {
		entitymanager.getTransaction().begin();
		Customer dfb = customerDAO.selectById(1);
		entitymanager.getTransaction().commit();
		Customer expectedFia = new Customer("FIA");
		customerDAO.update(dfb, expectedFia);
		entitymanager.getTransaction().begin();
		Customer actualFia = customerDAO.selectById(1);
		entitymanager.getTransaction().commit();
		assertEquals(expectedFia.getName(), actualFia.getName());
	}
	
	@Test(expected = NoResultException.class)
	public void testCreateAndDeleteCustomer() {
		
		Customer monsterInc = new Customer("Monster INC");
		entitymanager.getTransaction().begin();
		Customer monsterIncWithId = customerDAO.create(monsterInc);
		entitymanager.getTransaction().commit();
	
		entitymanager.getTransaction().begin();
		customerDAO.delete(monsterIncWithId);
		entitymanager.getTransaction().commit();
		
		try {
			entitymanager.getTransaction().begin();
			Customer shouldNotExist = customerDAO.selectById(monsterIncWithId.getId());
			entitymanager.getTransaction().commit();
		} catch (NoResultException e) {
			entitymanager.getTransaction().commit();
			throw new NoResultException("Exception wurde korrekt gewurfen");
		}
	}
	
	@Test
	public void testSelectAllCustomer() {
		entitymanager.getTransaction().begin();
		List<Customer> customerList = customerDAO.selectAllCustomers();
		entitymanager.getTransaction().commit();
		assertEquals("DFB", customerList.get(0).getName());
		assertEquals(1, customerList.size());
	}

	@After
	public void tearDown() throws SQLException, DataSetException, FileNotFoundException, IOException {

		IDataSet fullDataSet = mDBUnitConnection.createDataSet();
		FlatXmlDataSet.write(fullDataSet, new FileOutputStream("./src/main/resources/DatenbankContent.xml"));

		mDBUnitConnection.close();
		
		entitymanager.close();
		emfactory.close();
	}
}
