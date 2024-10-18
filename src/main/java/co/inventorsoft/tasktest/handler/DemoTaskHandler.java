package co.inventorsoft.tasktest.handler;

import co.inventorsoft.tasktest.model.TaskScheduleRule;
import co.inventorsoft.tasktest.provider.CsvTaskDataProvider;
import co.inventorsoft.tasktest.provider.DateProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class DemoTaskHandler {

    private static final String LAGOS_TIME_ZONE_ID = "Africa/Lagos";

    private final ActionHandler actionHandler;
    private final CsvTaskDataProvider csvTaskDataProvider;
    private final DateProvider dateProvider;

    public void executeDemoTask() {
        LocalDateTime currentDateTime = dateProvider.getCurrentDateTime(ZoneId.of(LAGOS_TIME_ZONE_ID))
                .withSecond(0).withNano(0);

        List<TaskScheduleRule> taskScheduleRules = csvTaskDataProvider.getTaskScheduleRules();

        for (var rule : taskScheduleRules) {
            Set<DayOfWeek> daysOfWeek = Arrays.stream(DayOfWeek.values())
                    .filter(dayOfWeek -> (rule.bitmask() & (1 << dayOfWeek.getValue() - 1)) != 0)
                    .collect(Collectors.toSet());
            boolean timeMatches = rule.time().equals(currentDateTime.toLocalTime());
            boolean dayOfWeekMatches = daysOfWeek.contains(currentDateTime.getDayOfWeek());

            if (timeMatches && dayOfWeekMatches) {
                actionHandler.performAction();
            }
        }
    }
}
