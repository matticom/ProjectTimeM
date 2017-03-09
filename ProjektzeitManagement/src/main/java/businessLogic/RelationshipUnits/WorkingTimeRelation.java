package businessLogic.RelationshipUnits;

import java.util.List;

import businessLogic.ExceptionsBL.ProjectDoesNotExist;
import businessLogic.ExceptionsBL.WorkingTimeDoesNotExist;
import businessLogic.interfaces.IWorkingTimeBL;
import model.WorkingTime;

public class WorkingTimeRelation {
		
	public void deleteProjectRelatedWorkingTimes(int projectId, IWorkingTimeBL workingTimeBL) throws ProjectDoesNotExist, WorkingTimeDoesNotExist {
		List<WorkingTime> workingTimeList = workingTimeBL.selectAllWorkingTimeByProject(projectId);
		for(WorkingTime workingTime: workingTimeList) {
			workingTimeBL.deleteWorkingTime(workingTime.getId());
		}
	}
}
