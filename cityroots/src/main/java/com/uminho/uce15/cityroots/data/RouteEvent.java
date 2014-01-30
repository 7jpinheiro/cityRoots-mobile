package com.uminho.uce15.cityroots.data;

import java.io.Serializable;

public class RouteEvent implements Serializable {

    private int order;
    private Event event;

    public RouteEvent(int order, Event event) {
        this.order = order;
        this.event = event;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
