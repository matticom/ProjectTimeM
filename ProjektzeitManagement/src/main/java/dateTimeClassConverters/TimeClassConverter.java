package dateTimeClassConverters;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;


import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class TimeClassConverter implements AttributeConverter<Instant, Date>{

	@Override
	public Date convertToDatabaseColumn(Instant timestamp) {
		return Date.from(timestamp);
	}

	@Override
	public Instant convertToEntityAttribute(Date dbDate) {
		ZoneId UTC = ZoneId.of("Z");
	    return dbDate.toInstant().atZone(UTC).toInstant();
	}

}
