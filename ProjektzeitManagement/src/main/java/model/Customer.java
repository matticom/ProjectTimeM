package model;

import java.util.ArrayList;
import java.util.Collections;
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

	@OneToMany(targetEntity = Project.class, mappedBy = "customer", cascade = CascadeType.REMOVE)
	private List<Project> projectList;

	public Customer() {
		// TODO Auto-generated constructor stub
	}

	public Customer(String name) {
		this.name = name;
		projectList = new ArrayList<Project>();
	}

	private Customer(int id, String name, List<Project> projectList) {
		this.id = id;
		this.name = name;
		this.projectList = projectList;
	}

	public static Customer createDbTestCustomer(int id, String name, List<Project> projectList) {
		if (projectList == null) {
			projectList = new ArrayList<Project>();
		}
		return new Customer(id, name, projectList);
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

	public boolean addProject(Project project) {
		boolean done = projectList.add(project);
		if (done) {
			project.setCustomer(this);
		}
		return done;
	}

	public boolean removeProject(Project project) {
		boolean done = projectList.remove(project);
		if (done) {
			project.setCustomer(null);
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
		Customer other = (Customer) obj;
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
		return "Customer [id=" + id + ", name=" + name + "]";
	}
}
