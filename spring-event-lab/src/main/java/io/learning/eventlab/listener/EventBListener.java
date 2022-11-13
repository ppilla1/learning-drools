package io.learning.eventlab.listener;

import io.learning.eventlab.event.EventB;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class EventBListener extends AbstractListener {
    public EventBListener(ApplicationEventPublisher publisher) {
        super(publisher);
    }

    @Async
    @EventListener
    public void onApplicationEvent(EventB event) {
        log.info("Thread Id {}, Type {}, Order Id {}", Thread.currentThread().getName(), event.getType(), event.getOrderId());
    }
}
