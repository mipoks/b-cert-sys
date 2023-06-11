package design.kfu.sunrise.util.converter;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

/**
 * @author Daniyar Zakiev
 */
@Component
public class InstantConverter {

    private final SimpleDateFormat formatter = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss");

    public String convert(Instant instant) {
        if (instant != null) {
            Date myDate = Date.from(instant);
            return formatter.format(myDate);
        } else {
            return null;
        }

    }
}
