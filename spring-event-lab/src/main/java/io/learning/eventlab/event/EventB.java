package io.learning.eventlab.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;
@Getter
public class EventB extends POCParentEvent{
    private String orderId;

    public EventB(UUID correlationId, Class type, String orderId) {
        super(correlationId, type);
        this.orderId = orderId;
    }
}
