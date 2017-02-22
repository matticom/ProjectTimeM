package model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Customer_ID")
	private int id;
	
	@Column(name = "Customer_Name")
	private String name;
	
	@OneToMany(targetEntity = Project.class, cascade = CascadeType.ALL)
	private List<Project> projectList;

	public Customer() {
		// TODO Auto-generated constructor stub
	}

	public Customer(int id, String name, List<Project> projectList) {
		this.id = id;
		this.name = name;
		this.projectList = projectList;
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

	public List<Project> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<Project> projectList) {
		this.projectList = projectList;
	}
}
