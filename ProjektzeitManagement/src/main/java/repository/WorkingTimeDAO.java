package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import model.Employee;
import model.Project;
import model.WorkingTime;
import model.WorkingTime_;

public class WorkingTimeDAO implements IWorkingTimeDAO{
	private EntityManager entitymanager;

	public WorkingTimeDAO(EntityManager entitymanager) {
		this.entitymanager = entitymanager;
	}

	public WorkingTime create(WorkingTime workingTime) {
		entitymanager.persist(workingTime);
		return workingTime;
	}

	public WorkingTime selectById(int id) {
		WorkingTime workingTime = entitymanager.find(WorkingTime.class, id);
		if (workingTime == null) {
			throw new NoResultException("Kein Zeiterfassungseintrag mit der ID gefunden");
		}
		return workingTime;
	}

	public List<WorkingTime> selectStartTimeBetween(long from, long until, Project project, Employee employee) {
		CriteriaBuilder criteriaBuilder = entitymanager.getCriteriaBuilder();
		CriteriaQuery<WorkingTime> criteriaQuery = criteriaBuilder.createQuery(WorkingTime.class);

		Root<WorkingTime> workingTime = criteriaQuery.from(WorkingTime.class);
		Predicate selectEmployee = criteriaBuilder.equal(workingTime.get(WorkingTime_.employee), employee);
		Predicate selectProject = criteriaBuilder.equal(workingTime.get(WorkingTime_.project), project);
		Predicate selectTime = criteriaBuilder.between(workingTime.get(WorkingTime_.startTime), from, until);
		Predicate whereFilter = criteriaBuilder.and(selectTime, selectProject, selectEmployee);
		criteriaQuery.select(workingTime).where(whereFilter);

		return entitymanager.createQuery(criteriaQuery).getResultList();
	}

	public List<WorkingTime> selectAllWorkingTimes(Project project, Employee employee) {
		CriteriaBuilder criteriaBuilder = entitymanager.getCriteriaBuilder();
		CriteriaQuery<WorkingTime> criteriaQuery = criteriaBuilder.createQuery(WorkingTime.class);

		Root<WorkingTime> workingTime = criteriaQuery.from(WorkingTime.class);
		Predicate selectEmployee = criteriaBuilder.equal(workingTime.get(WorkingTime_.employee), employee);
		Predicate selectProject = criteriaBuilder.equal(workingTime.get(WorkingTime_.project), project);
		Predicate whereFilter = criteriaBuilder.and(selectProject, selectEmployee);
		criteriaQuery.select(workingTime).where(whereFilter);

		return entitymanager.createQuery(criteriaQuery).getResultList();
	}

	public WorkingTime update(WorkingTime workingTime, WorkingTime newWorkingTime) {
		workingTime.setStartTime(newWorkingTime.getStartTime());
		workingTime.setEndTime(newWorkingTime.getEndTime());
		workingTime.setBreakTime(newWorkingTime.getBreakTime());
		workingTime.setComment(newWorkingTime.getComment());
		workingTime.setEmployee(newWorkingTime.getEmployee());
		workingTime.setProject(newWorkingTime.getProject());
		return workingTime;
	}

	public void delete(WorkingTime workingTime) {
		entitymanager.remove(workingTime);
	}
}
