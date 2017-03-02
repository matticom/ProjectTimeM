package businessLogic.ExceptionsBL;

import java.time.Instant;

public class BreakTimeException extends Exception {
	Instant startTime;
	Instant endTime;
	int breaktime;
	
	public BreakTimeException(Instant startTime, Instant endTime, int breaktime) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.breaktime = breaktime;
	}
	
	public String getMessage() {
		return "The difference between endtime " + endTime + " and starttime " + startTime + " is less than or equals breaktime " + breaktime;
	}

	public Instant getStartTime() {
		return startTime;
	}

	public Instant getEndTime() {
		return endTime;
	}

	public int getBreaktime() {
		return breaktime;
	}
}
