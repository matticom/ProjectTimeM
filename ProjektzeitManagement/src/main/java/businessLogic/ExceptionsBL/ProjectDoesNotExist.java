package businessLogic.ExceptionsBL;

public class ProjectDoesNotExist extends Exception {

	protected String name;
	protected int id;

	public ProjectDoesNotExist(String name) {
		this.name = name;
	}

	public ProjectDoesNotExist(int id) {
		this.id = id;
	}

	public String getMessage() {
		if (name != null) {
			return "Project name doesn't exist: '" + name + "'";
		} else {
			return "Project ID doesn't exist: " + id;
		}
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}
}
