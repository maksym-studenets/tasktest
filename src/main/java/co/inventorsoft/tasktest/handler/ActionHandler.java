package co.inventorsoft.tasktest.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ActionHandler {

    public void performAction() {
        log.info("Performing some action");
    }
}
