package timestampClassConverters;

import java.time.Instant;

public class TimeConvertErsatz {

	public static Long instantToLong(Instant timestamp) {
		return timestamp.getEpochSecond();
	}

	public static Instant longToInstant(Long dbDate) {
	    return Instant.ofEpochSecond(dbDate);
	}
}
