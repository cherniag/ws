package com.gc.ws.events;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
public abstract class Event {

    public String sessionId;

    public Event(String sessionId) {
        this.sessionId = sessionId;
    }


    @Override
    public String toString() {
        return "Event{" +
                "sessionId='" + sessionId + '\'' +
                '}';
    }
}
