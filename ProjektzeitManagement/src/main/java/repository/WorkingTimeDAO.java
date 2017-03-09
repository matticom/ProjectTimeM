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
import repository.interfaces.IWorkingTimeDAO;

public class WorkingTimeDAO implements IWorkingTimeDAO {
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
			throw new NoResultException("No workingtime with this Id was found");
		}
		return workingTime;
	}

	public List<WorkingTime> selectStartTimeBetween(long from, long to, Project project, Employee employee) {
		CriteriaBuilder criteriaBuilder = entitymanager.getCriteriaBuilder();
		CriteriaQuery<WorkingTime> criteriaQuery = criteriaBuilder.createQuery(WorkingTime.class);

		Root<WorkingTime> workingTime = criteriaQuery.from(WorkingTime.class);
		Predicate selectEmployee = criteriaBuilder.equal(workingTime.get(WorkingTime_.employee), employee);
		Predicate selectProject = criteriaBuilder.equal(workingTime.get(WorkingTime_.project), project);
		Predicate selectTime = criteriaBuilder.between(workingTime.get(WorkingTime_.startTime), from, to);
		Predicate whereFilter = criteriaBuilder.and(selectTime, selectProject, selectEmployee);
		criteriaQuery.select(workingTime).where(whereFilter);

		return entitymanager.createQuery(criteriaQuery).getResultList();
	}

	public List<WorkingTime> plausibilityCheckForNewTime(long newTime, Project project, Employee employee) {
		CriteriaBuilder criteriaBuilder = entitymanager.getCriteriaBuilder();
		CriteriaQuery<WorkingTime> criteriaQuery = criteriaBuilder.createQuery(WorkingTime.class);

		Root<WorkingTime> workingTime = criteriaQuery.from(WorkingTime.class);
		Predicate selectEmployee = criteriaBuilder.equal(workingTime.get(WorkingTime_.employee), employee);
		Predicate selectProject = criteriaBuilder.equal(workingTime.get(WorkingTime_.project), project);
		Predicate checkStartTime = criteriaBuilder.lessThanOrEqualTo(workingTime.get(WorkingTime_.startTime), newTime);
		Predicate checkEndTime = criteriaBuilder.greaterThanOrEqualTo(workingTime.get(WorkingTime_.endTime), newTime);
		Predicate whereFilter = criteriaBuilder.and(checkStartTime, checkEndTime, selectProject, selectEmployee);
		criteriaQuery.select(workingTime).where(whereFilter);

		return entitymanager.createQuery(criteriaQuery).getResultList();
	}

	public List<WorkingTime> selectAllWorkingTimes(Project project, Employee employee) {
		CriteriaBuilder criteriaBuilder = entitymanager.getCriteriaBuilder();
		CriteriaQuery<WorkingTime> criteriaQuery = criteriaBuilder.createQuery(WorkingTime.class);

		Root<WorkingTime> workingTime = criteriaQuery.from(WorkingTime.class);
		Predicate whereFilter;
		if (employee == null) {
			whereFilter = criteriaBuilder.equal(workingTime.get(WorkingTime_.project), project);
		} else if (project == null) {
			whereFilter = criteriaBuilder.equal(workingTime.get(WorkingTime_.employee), employee);
		} else {
			Predicate selectEmployee = criteriaBuilder.equal(workingTime.get(WorkingTime_.employee), employee);
			Predicate selectProject = criteriaBuilder.equal(workingTime.get(WorkingTime_.project), project);
			whereFilter = criteriaBuilder.and(selectProject, selectEmployee);
		}
		criteriaQuery.select(workingTime).where(whereFilter);

		return entitymanager.createQuery(criteriaQuery).getResultList();
	}

	public WorkingTime update(WorkingTime workingTime, WorkingTime newWorkingTime) {
		workingTime.setStartTime(newWorkingTime.getStartTime());
		workingTime.setEndTime(newWorkingTime.getEndTime());
		workingTime.setBreakTime(newWorkingTime.getBreakTime());
		workingTime.setComment(newWorkingTime.getComment());
		return workingTime;
	}

	public void delete(WorkingTime workingTime) {
		entitymanager.remove(workingTime);
	}
}
