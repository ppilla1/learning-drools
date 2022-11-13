package io.learning.eventlab.listener;

import io.learning.eventlab.event.POCParentEvent;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;

@Getter
@Component
@Log4j2
public class GlobalListener extends AbstractListener {
    private Queue<POCParentEvent> eventTracker = new LinkedList<>();

    public GlobalListener(ApplicationEventPublisher publisher) {
        super(publisher);
    }

    @Async
    @EventListener
    public void onApplicationEvent(POCParentEvent event) {
        eventTracker.add(event);
        log.info("Thread Id {}, Type : {}, Correlation Id {}",Thread.currentThread().getName(), event.getType(), event.getCorrelationId());
    }
}