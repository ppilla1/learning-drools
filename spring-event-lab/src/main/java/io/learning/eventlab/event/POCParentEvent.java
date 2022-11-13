package io.learning.eventlab.event;

import lombok.Getter;

import java.util.UUID;

@Getter
public class POCParentEvent<T>{
    private final UUID correlationId;
    private final Class<T> type;

    public POCParentEvent(UUID correlationId, Class<T> type) {
        this.correlationId = correlationId;
        this.type = type;
    }
}
