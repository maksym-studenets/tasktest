package co.inventorsoft.tasktest.provider;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class DateProvider {

    public LocalDateTime getCurrentDateTime(ZoneId zoneId) {
        return LocalDateTime.now(zoneId);
    }
}
