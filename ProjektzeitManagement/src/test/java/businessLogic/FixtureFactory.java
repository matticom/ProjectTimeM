package businessLogic;

import java.util.ArrayList;
import java.util.List;

import model.Customer;
import model.Employee;
import model.Project;
import model.WorkingTime;

public class FixtureFactory {

	private Customer customerDfb;
	private Employee employeeTavoSiller;
	private Employee employeeGuillaumeFournier;
	private Project projectDfbWebsite;
	private Project projectDfbEcommerce;
	private WorkingTime workingTime1;
	private WorkingTime workingTime2;
	private WorkingTime workingTime3;
	private WorkingTime workingTime4;
	private List<WorkingTime> websiteWTList;
	
	public FixtureFactory() {
		buildupFixture();
	}
	
	public Project newProjectWithRelationship(int id) {
		if (id == 0) {
			return projectDfbWebsite;
		} else {
			return projectDfbEcommerce;
		}
	}
	
	public Customer newCustomerWithRelationship() {
		return customerDfb;
	}
	
	public List<WorkingTime> newWorkTimeListWithRelationship() {
		return websiteWTList;
	}
	
	public Employee newEmloyeeWithRelationship(int id) {
		if (id == 0) {
			return employeeTavoSiller;
		} else {
			return employeeGuillaumeFournier;
		}
	}
	
	public WorkingTime newWorkingTimeWithRelationship(int id) {
		switch (id)
		{
		case 0:
			return workingTime1;
		case 1:
			return workingTime2;
		case 2:
			return workingTime3;
		default:
			return workingTime4;
		}
	}
	
	private void buildupFixture() {
		customerDfb = new Customer("DFB");
		employeeTavoSiller = new Employee("Tavo", "Siller");
		employeeGuillaumeFournier = new Employee("Guillaume", "Fournier");
		projectDfbWebsite = new Project("DFB Webseite", 1486731600, 1518267600, new Customer());
		projectDfbEcommerce = new Project("DFB E-Commerce", 1489731600, 1519267600, new Customer());
		workingTime1 = new WorkingTime(1486731600l, employeeTavoSiller, projectDfbWebsite);
		workingTime2 = new WorkingTime(1586731600l, employeeGuillaumeFournier, projectDfbWebsite);
		workingTime3 = new WorkingTime(1686731600l, employeeTavoSiller, projectDfbWebsite);
		workingTime4 = new WorkingTime(1786731600l, employeeTavoSiller, projectDfbEcommerce);
		websiteWTList = new ArrayList<WorkingTime>();
		websiteWTList.add(workingTime1);
		websiteWTList.add(workingTime2);
		websiteWTList.add(workingTime3);
		setModelObjectIDs();
		relateModelObjects();
	}
	
	private void setModelObjectIDs() {
		customerDfb.setId(1);
		employeeTavoSiller.setId(2);
		employeeGuillaumeFournier.setId(3);
		projectDfbWebsite.setId(4);
		projectDfbEcommerce.setId(5);
		workingTime1.setId(6);
		workingTime2.setId(7);
		workingTime3.setId(8);
		workingTime4.setId(9);
	}
	private void relateModelObjects() {
		customerDfb.addProject(projectDfbWebsite);
		customerDfb.addProject(projectDfbEcommerce);
		
		employeeTavoSiller.addProject(projectDfbWebsite);
		employeeTavoSiller.addProject(projectDfbEcommerce);
		employeeGuillaumeFournier.addProject(projectDfbWebsite);
	}
	 
}
