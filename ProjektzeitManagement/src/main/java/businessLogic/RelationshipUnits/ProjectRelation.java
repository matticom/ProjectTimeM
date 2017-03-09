package businessLogic.RelationshipUnits;

import businessLogic.ExceptionsBL.ProjectDoesNotExist;
import businessLogic.ExceptionsBL.WorkingTimeDoesNotExist;
import businessLogic.interfaces.IProjectBL;
import businessLogic.interfaces.IWorkingTimeBL;

public class ProjectRelation {
	protected IProjectBL projectBL;

	public ProjectRelation(IProjectBL projectBL) {
		this.projectBL = projectBL;
	}
	
	public void deleteProject(int projectId, IWorkingTimeBL workingTimeBL, WorkingTimeRelation workingTimeRelation) throws ProjectDoesNotExist, WorkingTimeDoesNotExist {
		workingTimeRelation.deleteProjectRelatedWorkingTimes(projectId, workingTimeBL);
		projectBL.deleteProject(projectId);
	}
}
