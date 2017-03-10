package businessLogic;

import businessLogic.ExceptionsBL.ProjectDoesNotExist;
import businessLogic.ExceptionsBL.WorkingTimeDoesNotExist;
import businessLogic.RelationshipUnits.ProjectRelation;
import businessLogic.RelationshipUnits.WorkingTimeRelation;
import businessLogic.interfaces.IWorkingTimeBL;

public class RelationshipBL {

	private WorkingTimeRelation workingTimeRelation;
	private ProjectRelation projectRelation;
	private IWorkingTimeBL workingTimeBL;
		
	public RelationshipBL(WorkingTimeRelation workingTimeRelation, ProjectRelation projectRelation, IWorkingTimeBL workingTimeBL) {
		this.workingTimeRelation = workingTimeRelation;
		this.projectRelation = projectRelation;
		this.workingTimeBL = workingTimeBL;
	}

	public void deleteRelatedProject(int projectId) throws ProjectDoesNotExist, WorkingTimeDoesNotExist {
		projectRelation.deleteProject(projectId, workingTimeBL, workingTimeRelation);
	}
	
	public void deleteProjectRelatedWorkingTimes(int projectId) throws ProjectDoesNotExist, WorkingTimeDoesNotExist {
		workingTimeRelation.deleteProjectRelatedWorkingTimes(projectId, workingTimeBL);
	}
}
