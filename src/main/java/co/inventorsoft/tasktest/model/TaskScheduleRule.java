package co.inventorsoft.tasktest.model;

import java.time.LocalTime;

public record TaskScheduleRule(LocalTime time, int bitmask) {
}
