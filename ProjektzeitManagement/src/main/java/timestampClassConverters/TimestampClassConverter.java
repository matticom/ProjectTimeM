package timestampClassConverters;

import java.time.Instant;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@Converter (autoApply=true)
public class TimestampClassConverter implements AttributeConverter<Instant, Long>{

	@Override
	public Long convertToDatabaseColumn(Instant timestamp) {
		return timestamp.getEpochSecond();
	}

	@Override
	public Instant convertToEntityAttribute(Long dbDate) {
	    return Instant.ofEpochSecond(dbDate);
	}
}
