package businessLogic.ExceptionsBL;

import java.time.Instant;

public class WorkingTimeDoesNotExist extends Exception {

	protected int id;
	protected Instant from;
	protected Instant to;
	
	public WorkingTimeDoesNotExist(int id) {
		this.id = id;
	}

	public WorkingTimeDoesNotExist(Instant from, Instant to) {
		this.from = from;
		this.to = to;
	}

	public String getMessage() {
		if (from == null) {
			return "WorkingTime ID doesn't exist: " + id;
		} else {
			return "WorkingTime in Interval from " + from + " to " + to + " doesn't exist";
		}
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
