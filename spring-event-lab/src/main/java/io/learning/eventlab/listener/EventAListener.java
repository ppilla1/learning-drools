package io.learning.eventlab.listener;

import io.learning.eventlab.event.EventA;
import io.learning.eventlab.event.EventB;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class EventAListener implements ApplicationListener<EventA> {

    private final ApplicationEventPublisher publisher;

    public EventAListener(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void onApplicationEvent(EventA event) {
        log.info("Thread Id {}, Type {}, Message {}", Thread.currentThread().getName(), event.getType(), event.getMessage());
        EventB eventB = new EventB(event);
        eventB.setCorrelationId(event.getCorrelationId());
        eventB.setType(EventB.class);
        eventB.setOrderId("1234");
        publisher.publishEvent(eventB);
    }
}
