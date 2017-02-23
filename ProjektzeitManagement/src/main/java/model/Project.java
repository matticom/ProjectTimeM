package model;

import java.time.Instant;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import timestampClassConverters.TimestampClassConverter;

@Entity
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Project_ID")
	private int id;
	
	@Column(name = "Project_Name")
	private String name;
	
	@Column(name = "Project_StartDate")
//	@Convert(converter = TimestampClassConverter.class)
	private Instant startDate;
	
	@Column(name = "Project_EndDate")
//	@Convert(converter = TimestampClassConverter.class)
	private Instant endDate;

	@ManyToMany()
	@JoinTable(
			name = "PROJECT_EMPLOYEES",
			joinColumns = @JoinColumn(name="Project_ID", referencedColumnName="Project_ID"),
			inverseJoinColumns = @JoinColumn(name="Employee_ID", referencedColumnName="Employee_ID")
			 )
	private List<Employee> employeeList;
	
	@OneToMany(targetEntity = WorkingTime.class, mappedBy = "project", cascade = CascadeType.ALL)
	private List<WorkingTime> workingTimeList;

	@JoinColumn(name = "Customer_ID_FK")
	@ManyToOne()
	private Customer customer;
	
	
	
	public Project() {
		// TODO Auto-generated constructor stub
	}
	
	public Project(String name, Instant startDate, Instant endDate) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
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

	public Instant getStartDate() {
		return startDate;
	}

	public void setStartDate(Instant startDate) {
		this.startDate = startDate;
	}

	public Instant getEndDate() {
		return endDate;
	}

	public void setEndDate(Instant endDate) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Project other = (Project) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Project [id=" + id + ", name=" + name + ", startDate=" + startDate + ", endDate=" + endDate + ", customer=" + customer + "]";
	}
}
