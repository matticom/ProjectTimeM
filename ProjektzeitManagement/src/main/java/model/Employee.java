package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Employee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Employee_ID")
	private int id;
	
	@Column(name = "Employee_FirstName")
	private String firstName;
	
	@Column(name = "Employee_LastName")
	private String lastName;
	
	@OneToMany(targetEntity = WorkingTime.class, mappedBy = "employee")//, cascade = CascadeType.ALL)
	private List<WorkingTime> workingTimeList;
	
	@ManyToMany(targetEntity = Project.class, mappedBy = "employeeList")//, cascade = CascadeType.ALL)
	private List<Project> projectList;

	public Employee() {
		// TODO Auto-generated constructor stub
	}

	public Employee(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
		workingTimeList = new ArrayList<WorkingTime>();
		projectList = new ArrayList<Project>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<WorkingTime> getWorkingTimeList() {
		return workingTimeList;
	}

	public boolean addWorkingTime(WorkingTime workingTime) {
		return workingTimeList.add(workingTime);
	}

	public boolean removeWorkingTime(WorkingTime workingTime) {
		boolean done = workingTimeList.remove(workingTime);
		if (done) {
			workingTime.setEmployee(null);
		}
		return done;
	}

	public void setWorkingTimeList(List<WorkingTime> workingTimeList) {
		this.workingTimeList = workingTimeList;
	}

	public List<Project> getProjectList() {
		return projectList;
	}

	public boolean addProject(Project project) {
		boolean done = projectList.add(project);
		if (done && !project.getEmployeeList().contains(this)) {
			project.addEmployee(this);
		}
		return done;
	}

	public boolean removeProject(Project project) {
		boolean done = projectList.remove(project);
		if (done && project.getEmployeeList().contains(this)) {
			project.getEmployeeList().remove(this);
		}
		return done;
	}
	
	public void setProjectList(List<Project> projectList) {
		this.projectList = projectList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + id;
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id != other.id)
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", workingTimeList=" + workingTimeList + ", projectList=" + projectList + "]";
	}
}
