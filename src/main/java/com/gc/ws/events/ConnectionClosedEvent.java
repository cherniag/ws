package com.gc.ws.events;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
public class ConnectionClosedEvent extends Event {

    public ConnectionClosedEvent(String sessionId) {
        super(sessionId);
    }


    @Override
    public String toString() {
        return "ConnectionClosedEvent{} " + super.toString();
    }
}
