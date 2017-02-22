package model;

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
	
	@OneToMany(targetEntity = WorkingTime.class, mappedBy = "employee", cascade = CascadeType.ALL)
	private List<WorkingTime> workingTimeList;
	
	@ManyToMany(targetEntity = Project.class, mappedBy = "employeeList", cascade = CascadeType.ALL)
	private List<Project> projectList;

	public Employee() {
		// TODO Auto-generated constructor stub
	}

	public Employee(int id, String firstName, String lastName, List<WorkingTime> workingTimeList, List<Project> projectList) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.workingTimeList = workingTimeList;
		this.projectList = projectList;
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

	public void setWorkingTimeList(List<WorkingTime> workingTimeList) {
		this.workingTimeList = workingTimeList;
	}

	public List<Project> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<Project> projectList) {
		this.projectList = projectList;
	}
}
