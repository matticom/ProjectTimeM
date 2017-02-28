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
//	@Convert(converter = TimestampClassConverter.class)
	private long startTime;
	
	@Column(name = "WorkingTime_EndTime")
//	@Convert(converter = TimestampClassConverter.class)
	private long endTime; 
	
	@Column(name = "WorkingTime_BreakTime_Seconds")
	private int breakTime;
	
	@Column(name = "Comments")
	private String comment;

	@JoinColumn(name = "Employee_ID_FK")
	@ManyToOne()
	private Employee employee;
	
	@JoinColumn(name = "Project_ID_FK")
	@ManyToOne()
	private Project project;
	
	
	public WorkingTime() {
		
	}

	public WorkingTime(long startTime, Employee employee, Project project) {
		this.startTime = startTime;
		this.employee = employee;
		this.project = project;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public long getStartTime() {
		return startTime;
	}


	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}


	public long getEndTime() {
		return endTime;
	}


	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}


	public int getBreakTime() {
		return breakTime;
	}


	public void setBreakTime(int breakTime) {
		this.breakTime = breakTime;
	}

	
	public String getComment() {
		return comment;
	}

	
	public void setComment(String comment) {
		this.comment = comment;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((project == null) ? 0 : project.hashCode());
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
		WorkingTime other = (WorkingTime) obj;
		if (id != other.id)
			return false;
		if (project == null) {
			if (other.project != null)
				return false;
		} else if (!project.equals(other.project))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WorkingTime [id=" + id + ", startTime=" + startTime + ", endTime=" + endTime + ", breakTime=" + breakTime + ", employee=" + employee + ", project=" + project + "]";
	}
}
