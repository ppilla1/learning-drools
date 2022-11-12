package io.learning.eventlab;

import io.learning.eventlab.event.EventA;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import java.util.UUID;

@Log4j2
@SpringBootTest
public class MainTest {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Test
    public void test() {
        log.info("Hello, SpringBoot !!");
        EventA eventA = new EventA(this);
        eventA.setType(EventA.class);
        eventA.setCorrelationId(UUID.randomUUID());
        eventA.setMessage("Hello Spring Event");
        publisher.publishEvent(eventA);
    }
}
