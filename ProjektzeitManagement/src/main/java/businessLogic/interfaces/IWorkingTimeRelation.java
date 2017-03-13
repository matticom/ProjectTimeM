package businessLogic.interfaces;

import businessLogic.ExceptionsBL.ProjectDoesNotExist;
import businessLogic.ExceptionsBL.WorkingTimeDoesNotExist;

public interface IWorkingTimeRelation {
	public void deleteProjectRelatedWorkingTimes(int projectId, IWorkingTimeBL workingTimeBL) throws ProjectDoesNotExist, WorkingTimeDoesNotExist;
}
