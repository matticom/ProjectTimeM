package model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import timestampClassConverters.TimestampClassConverter;

@Entity
public class WorkingTime {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "WorkingTime_ID")
	private int id;
	
	@Column(name = "WorkingTime_StartTime")
	@Convert(converter = TimestampClassConverter.class)
	private Instant startTime;
	
	@Column(name = "WorkingTime_EndTime")
	@Convert(converter = TimestampClassConverter.class)
	private Instant endTime; 
	
	@Column(name = "WorkingTime_BreakTime_Seconds")
	private int breakTime;

	@JoinColumn(name = "Employee_ID_FK")
	@ManyToOne()
	private Employee employee;
	
	@JoinColumn(name = "Project_ID_FK")
	@ManyToOne()
	private Project project;
	
	
	public WorkingTime() {
		
	}

	public WorkingTime(int id, Instant startTime, Instant endTime, int breakTime, Employee employee, Project project) {
		this.id = id;
		this.startTime = startTime;
		this.endTime = endTime;
		this.breakTime = breakTime;
		this.employee = employee;
		this.project = project;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Instant getStartTime() {
		return startTime;
	}


	public void setStartTime(Instant startTime) {
		this.startTime = startTime;
	}


	public Instant getEndTime() {
		return endTime;
	}


	public void setEndTime(Instant endTime) {
		this.endTime = endTime;
	}


	public int getBreakTime() {
		return breakTime;
	}


	public void setBreakTime(int breakTime) {
		this.breakTime = breakTime;
	}


	public Employee getEmployee() {
		return employee;
	}


	public void setEmployee(Employee employee) {
		this.employee = employee;
	}


	public Project getProject() {
		return project;
	}


	public void setProject(Project project) {
		this.project = project;
	}
}
