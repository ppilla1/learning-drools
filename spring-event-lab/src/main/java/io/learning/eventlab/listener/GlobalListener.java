package io.learning.eventlab.listener;

import io.learning.eventlab.event.POCParentEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class GlobalListener implements ApplicationListener<POCParentEvent> {

    @Override
    public void onApplicationEvent(POCParentEvent event) {
        log.info("Thread Id {}, Type : {}, Correlation Id {}",Thread.currentThread().getName(), event.getType(), event.getCorrelationId());
    }
}