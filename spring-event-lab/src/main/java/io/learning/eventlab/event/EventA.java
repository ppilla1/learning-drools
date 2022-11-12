package io.learning.eventlab.event;

public class EventA  extends POCParentEvent{
    private String message;

    public EventA(Object source) {
        super(source);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
