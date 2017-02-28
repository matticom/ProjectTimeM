package businessLogic.ExceptionsBL;

public class CustomerAlreadyExisting extends Exception {

	protected String name;

	public CustomerAlreadyExisting(String name) {
		this.name = name;
	}
	
	public String getMessage() {
		return "Customer name is already existing: '" + name + "'";
	}

	public String getName() {
		return name;
	}
}
