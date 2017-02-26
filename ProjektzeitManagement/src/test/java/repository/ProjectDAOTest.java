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

import model.Customer;
import model.Project;

public class ProjectDAOTest {
	
	public static final Logger log = LoggerFactory.getLogger("ProjectDAOTest.class");

	private static IDatabaseConnection mDBUnitConnection;
	private static IDataSet startDataset;

	private static EntityManagerFactory emfactory;
	private static EntityManager entitymanager;
	private static Connection connection;

	private ProjectDAO projectDAO;
	private Instant startDate;
	private Instant endDate;

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

		projectDAO = new ProjectDAO(entitymanager);
		startDate = LocalDateTime.of(2017, Month.APRIL, 1, 12, 0).toInstant(ZoneOffset.UTC);
		endDate = LocalDateTime.of(2018, Month.APRIL, 1, 12, 0).toInstant(ZoneOffset.UTC);

		try {
			DatabaseOperation.CLEAN_INSERT.execute(mDBUnitConnection, startDataset);
		} catch (Exception e) {
			log.error("Exception bei DBUnit/DatabaseOperation: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testCreateAndSelectByID() { //////////////// hierrrrrrrrrrrrrrrrrr!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		Project actual = new Project("Projektmanagement", startDate, endDate);
		
		entitymanager.getTransaction().begin();
		int id = projectDAO.create(actual).getId();
		entitymanager.getTransaction().commit();
		entitymanager.getTransaction().begin();
		Project expected = projectDAO.selectById(id);
		entitymanager.getTransaction().commit();
//		log.error(String.valueOf(startDate.getEpochSecond()));
//		log.error(LocalDateTime.ofInstant(startDate, ZoneId.of("GMT")).toString());
//		log.error(String.valueOf(actual.getStartDate().getEpochSecond()));
		assertEquals(expected.getName(), actual.getName());
		assertEquals(startDate, actual.getStartDate());
		assertEquals(endDate, actual.getEndDate());
	}
	
	@Test
	public void testSelectByID() {
		
		entitymanager.getTransaction().begin();
		Project actual = projectDAO.selectById(2);
		entitymanager.getTransaction().commit();
		assertEquals("DFB Webseite", actual.getName());
		
	}
	
	@Test(expected = NoResultException.class)
	public void testNoResultExceptionAtSelectById() {
		try {
			entitymanager.getTransaction().begin();
			projectDAO.selectById(1343);
			entitymanager.getTransaction().commit();
		} catch (Exception e) {
			entitymanager.getTransaction().commit();
			throw new NoResultException("Exception wurde korrekt gewurfen");
		}
	}
//	
//	@Test
//	public void testSelectByName() {
//		entitymanager.getTransaction().begin();
//		Project actual = projectDAO.selectByName("DFB");
//		entitymanager.getTransaction().commit();
//		assertEquals("DFB", actual.getName());
//	}
//	
//	@Test(expected = NoResultException.class)
//	public void testNoResultExceptionAtSelectByName() {
//		try {
//			entitymanager.getTransaction().begin();
//			projectDAO.selectByName("Monster INC");
//			entitymanager.getTransaction().commit();
//		} catch (Exception e) {
//			entitymanager.getTransaction().commit();
//			throw new NoResultException("Exception wurde korrekt gewurfen");
//		}
//	}
//		
//	@Test
//	public void testUpdateProject() {
//		entitymanager.getTransaction().begin();
//		Project dfb = projectDAO.selectById(1);
//		entitymanager.getTransaction().commit();
//		projectDAO.update(dfb, "FIA");
//		entitymanager.getTransaction().begin();
//		Project fia = projectDAO.selectById(1);
//		entitymanager.getTransaction().commit();
//		assertEquals("FIA", fia.getName());
//	}
//	
//	@Test(expected = NoResultException.class)
//	public void testCreateAndDeleteProject() {
//		
//		Project monsterInc = new Project("Monster INC");
//		entitymanager.getTransaction().begin();
//		Project monsterIncWithId = projectDAO.create(monsterInc);
//		entitymanager.getTransaction().commit();
//	
//		entitymanager.getTransaction().begin();
//		projectDAO.delete(monsterIncWithId);
//		entitymanager.getTransaction().commit();
//		
//		try {
//			entitymanager.getTransaction().begin();
//			Project shouldNotExist = projectDAO.selectById(monsterIncWithId.getId());
//			entitymanager.getTransaction().commit();
//		} catch (NoResultException e) {
//			entitymanager.getTransaction().commit();
//			throw new NoResultException("Exception wurde korrekt gewurfen");
//		}
//	}
//	
//	@Test
//	public void testSelectAllProject() {
//		entitymanager.getTransaction().begin();
//		List<Project> projectList = projectDAO.selectAllProject();
//		entitymanager.getTransaction().commit();
//		assertEquals("DFB", projectList.get(0).getName());
//		assertEquals(1, projectList.size());
//	}

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
