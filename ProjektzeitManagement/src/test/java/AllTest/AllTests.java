package AllTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import businessLogic.CustomerBLTest;
import businessLogic.EmployeeBLTest;
import businessLogic.ProjectBLTest;
import businessLogic.RelationshipBLTest;
import businessLogic.WorkingTimeBLTest;
import dateTimeClassConverter.TimestampClassConverterTest;
import repository.CustomerDAOTest;

@RunWith(Suite.class)
@SuiteClasses({ 
	TimestampClassConverterTest.class,
	CustomerBLTest.class,
	WorkingTimeBLTest.class,
	RelationshipBLTest.class,
	ProjectBLTest.class,
	EmployeeBLTest.class
})

public class AllTests {

}
