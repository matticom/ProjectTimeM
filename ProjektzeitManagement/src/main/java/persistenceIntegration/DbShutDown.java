package persistenceIntegration;

public class DbShutDown extends fit.ColumnFixture {
	private DatabaseConnect dbConnect;
	
	public DbShutDown() {
		dbConnect = DatabaseConnect.dbConnectInstance();
		dbConnect.shutDown();
		dbConnect = null;
		System.out.println("ShutDown is done");
	}
}
