package dateTimeClassConverter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import timestampClassConverters.TimestampClassConverter;

public class TimestampClassConverterTest {

	private TimestampClassConverter timestampConverter;
	
	@Before
	public void setUp() {
		timestampConverter = new TimestampClassConverter();
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
	
	@Test
	public void testOldClassToLatestClass() {
		long exceptedEpochSeconds = 1392033600l;
		Instant actualTimestamp = timestampConverter.convertToEntityAttribute(exceptedEpochSeconds);
		assertEquals(exceptedEpochSeconds, actualTimestamp.getEpochSecond());
	}
	
	@Test
	public void testLatestClassToOldClass() {
		LocalDateTime ldt = LocalDateTime.of(2018, Month.FEBRUARY, 10, 8, 45, 0);
		Instant exceptedTimestamp = ldt.toInstant(ZoneOffset.UTC);
		System.out.println(exceptedTimestamp.getEpochSecond());
		long actualEpochSeconds = timestampConverter.convertToDatabaseColumn(exceptedTimestamp);
		assertEquals(exceptedTimestamp.getEpochSecond(), actualEpochSeconds);
	}
}
