package repository;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import dateTimeClassConverter.TimestampClassConverterTest;
import repository.CustomerDAOTest;

@RunWith(Suite.class)
@SuiteClasses({ 
	CustomerDAOTest.class,
	EmployeeDAOTest.class
})

public class AllRepositoryTests {

}
