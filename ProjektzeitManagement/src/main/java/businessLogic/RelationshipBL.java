package businessLogic;

import businessLogic.ExceptionsBL.ProjectDoesNotExist;
import businessLogic.ExceptionsBL.WorkingTimeDoesNotExist;
import businessLogic.RelationshipUnits.WorkingTimeRelation;
import businessLogic.interfaces.IWorkingTimeBL;

public class RelationshipBL {

	private WorkingTimeRelation workingTimeRelation;
	private IWorkingTimeBL workingTimeBL;
		
	public RelationshipBL(WorkingTimeRelation workingTimeRelation, IWorkingTimeBL workingTimeBL) {
		this.workingTimeRelation = workingTimeRelation;
		this.workingTimeBL = workingTimeBL;
	}
	
	public void deleteProjectRelatedWorkingTimes(int projectId) throws ProjectDoesNotExist, WorkingTimeDoesNotExist {
		workingTimeRelation.deleteProjectRelatedWorkingTimes(projectId, workingTimeBL);
	}
}
