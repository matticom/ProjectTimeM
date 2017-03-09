package AllTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import businessLogic.CustomerBLTest;
import businessLogic.ProjectBLTest;
import businessLogic.WorkingTimeRelationTest;
import businessLogic.WorkingTimeBLTest;
import dateTimeClassConverter.TimestampClassConverterTest;
import repository.CustomerDAOTest;

@RunWith(Suite.class)
@SuiteClasses({ 
	TimestampClassConverterTest.class,
	CustomerBLTest.class,
	WorkingTimeBLTest.class,
	WorkingTimeRelationTest.class,
	ProjectBLTest.class
})

public class AllTests {

}
