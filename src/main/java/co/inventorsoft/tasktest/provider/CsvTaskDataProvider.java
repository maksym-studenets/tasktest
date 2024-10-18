package co.inventorsoft.tasktest.provider;

import co.inventorsoft.tasktest.model.TaskScheduleRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CsvTaskDataProvider {

    public List<TaskScheduleRule> getTaskScheduleRules() {
        List<TaskScheduleRule> taskScheduleRules = new ArrayList<>();
        try (var inputStream = getClass().getClassLoader().getResourceAsStream("task-schedule.csv");
             var bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] taskData = line.split(",");
                LocalTime time = LocalTime.parse(taskData[0]);
                int bitmask = Integer.parseInt(taskData[1]);
                taskScheduleRules.add(new TaskScheduleRule(time, bitmask));
            }
        } catch (Exception e) {
            log.error("Error reading CSV file with task schedule rules. ", e);
            throw new RuntimeException("Cannot read CSV file with task schedule rules");
        }
        return taskScheduleRules;
    }
}
