package io.learning.eventlab.listener;

import io.learning.eventlab.event.EventA;
import io.learning.eventlab.event.EventB;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class EventAListener extends AbstractListener {

    public EventAListener(ApplicationEventPublisher publisher) {
        super(publisher);

    }

    @Async
    @EventListener
    public void onApplicationEvent(EventA event) {
        log.info("Thread Id {}, Type {}, Message {}", Thread.currentThread().getName(), event.getType(), event.getMessage());

        EventB eventB = new EventB(
                event.getCorrelationId(),
                EventB.class,
                "1234");

        publish(eventB);
    }
}
