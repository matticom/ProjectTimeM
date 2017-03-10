package persistenceIntegration;

public class DbDeployment extends fit.ColumnFixture {
	private DatabaseConnect dbConnect;
	
	public DbDeployment() {
		dbConnect = DatabaseConnect.dbConnectInstance();
		dbConnect.initializeDataBase();
	}
}
