package io.learning.eventlab.event;

public class EventB extends POCParentEvent{
    private String orderId;

    public EventB(Object source) {
        super(source);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
