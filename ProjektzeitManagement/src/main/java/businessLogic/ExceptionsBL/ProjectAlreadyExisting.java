package businessLogic.ExceptionsBL;

public class ProjectAlreadyExisting extends Exception {

	protected String name;

	public ProjectAlreadyExisting(String name) {
		this.name = name;
	}
	
	public String getMessage() {
		return "Project name is already existing: '" + name + "'";
	}

	public String getName() {
		return name;
	}
}
