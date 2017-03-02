package businessLogic.ExceptionsBL;

public class CustomerDoesNotExist extends Exception {

	protected String name;
	protected int id;

	public CustomerDoesNotExist(String name) {
		this.name = name;
	}

	public CustomerDoesNotExist(int id) {
		this.id = id;
	}

	public String getMessage() {
		if (name != null) {
			return "Customer name doesn't exist: '" + name + "'";
		} else {
			return "Customer ID doesn't exist: " + id;
		}
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}
}
