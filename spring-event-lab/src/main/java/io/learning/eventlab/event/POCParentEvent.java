package io.learning.eventlab.event;

import org.springframework.context.ApplicationEvent;

import java.util.UUID;


public class POCParentEvent<T>  extends ApplicationEvent {
    private UUID correlationId;
    private Class<T> type;

    public POCParentEvent(Object source) {
        super(source);
    }

    public UUID getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(UUID correlationId) {
        this.correlationId = correlationId;
    }

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }
}
