package io.learning.eventlab.listener;

import io.learning.eventlab.event.EventB;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class EventBListener implements ApplicationListener<EventB> {

    private final ApplicationEventPublisher publisher;

    public EventBListener(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void onApplicationEvent(EventB event) {
        log.info("Thread Id {}, Type {}, Order Id {}", Thread.currentThread().getName(), event.getType(), event.getOrderId());
    }
}
