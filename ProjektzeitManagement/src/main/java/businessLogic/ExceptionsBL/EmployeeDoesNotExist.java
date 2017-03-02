package businessLogic.ExceptionsBL;

public class EmployeeDoesNotExist extends Exception {

	protected String firstname;
	protected String lastname;
	protected int id;

	public EmployeeDoesNotExist(String firstname, String lastname) {
		this.firstname = firstname;
		this.lastname = lastname;
	}

	public EmployeeDoesNotExist(int id) {
		this.id = id;
	}

	public String getMessage() {
		if (firstname != null && lastname != null) {
			return "Employee name is already existing: '" + firstname + " " + lastname + "'";
		} else {
			return "Employee ID doesn't exist: " + id;
		}
	}

	public String getName() {
		return firstname + " " + lastname;
	}

	public int getId() {
		return id;
	}
}
