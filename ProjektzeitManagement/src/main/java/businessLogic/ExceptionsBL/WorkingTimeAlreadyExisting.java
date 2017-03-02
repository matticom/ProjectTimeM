package businessLogic.ExceptionsBL;

import java.time.Instant;

public class WorkingTimeAlreadyExisting extends Exception {

	protected Instant instantOfTime;
	protected int id;
	protected Instant from;
	protected Instant to;

	public WorkingTimeAlreadyExisting(Instant instantOfTime, int id) {
		this.instantOfTime = instantOfTime;
		this.id = id;
	}
	
	public WorkingTimeAlreadyExisting(Instant from, Instant to, int id) {
		this.from = from;
		this.to = to;
		this.id = id;
	}
	
	public String getMessage() {
		if (instantOfTime != null) {
		return "Instant of time (" + instantOfTime + ") of this WorkingTime is within a already existing WorkingTime with ID: '" + id + "'";
		} else {
			return "Either starttime " + from + " or endtime " + to + "of the update is within the WorkingTime with ID: '" + id + "'";
		}
		
	}

	public Instant getInstantOfTime() {
		return instantOfTime;
	}

	public int getId() {
		return id;
	}

	public Instant getFrom() {
		return from;
	}

	public Instant getTo() {
		return to;
	}
}
