package dateTimeClassConverters;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class DateClassConverter implements AttributeConverter<LocalDate, Date>{

	@Override
	public Date convertToDatabaseColumn(LocalDate localDate) {
		LocalTime stdLocalTime = LocalTime.of(12,0);
		LocalDateTime localDateTime = LocalDateTime.of(localDate, stdLocalTime);
		return Date.from(localDateTime.toInstant(ZoneOffset.ofHours(-2)));
	}

	@Override
	public LocalDate convertToEntityAttribute(Date dbDate) {
		ZoneId UTC = ZoneId.of("Z");
	    Instant instant = dbDate.toInstant().atZone(UTC).toInstant();
	    return LocalDateTime.ofInstant(instant, UTC).toLocalDate();
	}

}
