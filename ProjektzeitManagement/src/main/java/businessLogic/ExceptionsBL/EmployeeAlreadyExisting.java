package businessLogic.ExceptionsBL;

public class EmployeeAlreadyExisting extends Exception {

	protected String firstname;
	protected String lastname;

	public EmployeeAlreadyExisting(String firstname, String lastname) {
		this.firstname = firstname;
		this.lastname = lastname;
	}
	
	public String getMessage() {
		return "Employee name is already existing: '" + firstname + " " + lastname + "'";
	}

	public String getName() {
		return firstname + " " + lastname;
	}
}
