package io.learning.eventlab;

import io.learning.eventlab.event.EventA;
import io.learning.eventlab.event.POCParentEvent;
import io.learning.eventlab.listener.GlobalListener;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Queue;
import java.util.UUID;

@Log4j2
@SpringBootTest
public class MainTest {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private GlobalListener globalListener;

    @Test
    public void test() throws InterruptedException {
        log.info("Hello, SpringBoot !!");
        EventA eventA = new EventA(UUID.randomUUID(), EventA.class, "Create Order");
        publisher.publishEvent(eventA);
        Thread.sleep(1000);
        var tracker = globalListener.getEventTracker();

        while(!tracker.isEmpty()) {
            POCParentEvent event = tracker.remove();
            log.info("Event type {}, CorrelationId {}", event.getType(), event.getCorrelationId());
        }
    }
}
