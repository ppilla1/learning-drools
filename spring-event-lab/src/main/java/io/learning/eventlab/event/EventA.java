package io.learning.eventlab.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Getter
public class EventA  extends POCParentEvent{
    private final String message;

    public EventA(UUID correlationId, Class type, String message) {
        super(correlationId, type);
        this.message = message;
    }
}
