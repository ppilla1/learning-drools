package io.learning.eventlab.listener;

import org.springframework.context.ApplicationEventPublisher;

public class AbstractListener {
    private final ApplicationEventPublisher publisher;

    public AbstractListener(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publish(Object event) {
        publisher.publishEvent(event);
    }
}
