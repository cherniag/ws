package com.gc.ws.events;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
public class SessionClosedEvent extends Event {

    public SessionClosedEvent(String sessionId) {
        super(sessionId);
    }


    @Override
    public String toString() {
        return "SessionClosedEvent{} " + super.toString();
    }
}
