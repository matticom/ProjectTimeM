package model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import dateTimeClassConverters.DateClassConverter;

@Entity
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Project_ID")
	private int id;
	
	@Column(name = "Project_Name")
	private String name;
	
	@Column(name = "Project_StartDate")
	@Convert(converter = DateClassConverter.class)
	private LocalDate startDate;
	
	@Column(name = "Project_EndDate")
	@Convert(converter = DateClassConverter.class)
	private LocalDate endDate;

	@ManyToMany()
	private List<Employee> employeeList;
	
	@OneToMany(targetEntity = WorkingTime.class, mappedBy = "project", cascade = CascadeType.ALL)
	private List<WorkingTime> workingTimeList;

	public Project() {
		// TODO Auto-generated constructor stub
	}

	public Project(int id, String name, LocalDate startDate, LocalDate endDate, List<Employee> employeeList, List<WorkingTime> workingTimeList) {
		this.id = id;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.employeeList = employeeList;
		this.workingTimeList = workingTimeList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public List<Employee> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<Employee> employeeList) {
		this.employeeList = employeeList;
	}

	public List<WorkingTime> getWorkingTimeList() {
		return workingTimeList;
	}

	public void setWorkingTimeList(List<WorkingTime> workingTimeList) {
		this.workingTimeList = workingTimeList;
	}
}
