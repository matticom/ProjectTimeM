package businessLogic.ExceptionsBL;

import java.time.Instant;

public class EndTimeEarlierThanStartTimeException extends Exception {

	Instant startTime;
	Instant endTime;
	
	public EndTimeEarlierThanStartTimeException(Instant startTime, Instant endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public String getMessage() {
		return "Starttime " + startTime + " is earlier than Endtime " + endTime;
	}

	public Instant getStartTime() {
		return startTime;
	}

	public Instant getEndTime() {
		return endTime;
	}
}
