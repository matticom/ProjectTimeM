package persistenceIntegration;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DatabaseConnect {
	
	private static DatabaseConnect dbConnect;
	public static final Logger log = LoggerFactory.getLogger("DatabaseConnection.class");
	private static IDatabaseConnection mDBUnitConnection;
	private static IDataSet startDataset;

	private static EntityManagerFactory emfactory;
	private static EntityManager entitymanager;
	private static Connection connection;
		
	private DatabaseConnect() {
		dbConnect = this;
	}
	
	public EntityManager getEntitymanager() {
		return entitymanager;
	}
	
	public static DatabaseConnect dbConnectInstance() {
		if (dbConnect == null) {
			return new DatabaseConnect();
		} else {
			return dbConnect;
		}
	}
	
	public void initializeDataBase() {

		emfactory = Persistence.createEntityManagerFactory("Eclipselink_JPA_Derby");
		entitymanager = emfactory.createEntityManager();
		connection = ((EntityManagerImpl) (entitymanager.getDelegate())).getServerSession().getAccessor().getConnection();
		
		try {
			mDBUnitConnection = new DatabaseConnection(connection);
			mDBUnitConnection.getConfig().setProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, true);
//			startDataset = new FlatXmlDataSetBuilder().build(new File("./src/test/resources/XML/testSetDAO_Start.xml"));
		} catch (Exception e) {
			log.error("Exception bei DBUnit/IDataConnection: " + e.getMessage());
			e.printStackTrace();
		}
//		try {
//			DatabaseOperation.CLEAN_INSERT.execute(mDBUnitConnection, startDataset);
//		} catch (Exception e) {
//			log.error("Exception bei DBUnit/DatabaseOperation: " + e.getMessage());
//			e.printStackTrace();
//		}
	}
	
	public void createDbImage() {
		try {
			IDataSet fullDataSet = mDBUnitConnection.createDataSet();
			FlatXmlDataSet.write(fullDataSet, new FileOutputStream("./dbFitTest.xml"));

		} catch (Exception e) {
			System.out.println("Es wurde eine Exception beim speichern der Datenbank geworfen: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void shutDown() {
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
