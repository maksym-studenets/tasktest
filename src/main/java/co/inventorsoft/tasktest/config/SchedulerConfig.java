package co.inventorsoft.tasktest.config;

import co.inventorsoft.tasktest.handler.DemoTaskHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {

    private final DemoTaskHandler demoTaskHandler;

    @Scheduled(cron = "${application.scheduled-jobs.demo.cron:0 * * * * *}")
    public void demoTask() {
        log.info("Started demo task");
        demoTaskHandler.executeDemoTask();
        log.info("Finished demo task");
    }
}
