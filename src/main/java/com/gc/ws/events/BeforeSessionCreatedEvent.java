package com.gc.ws.events;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
public class BeforeSessionCreatedEvent extends Event {
    private String guestSessionId;

    public BeforeSessionCreatedEvent(String sessionId, String guestSessionId) {
        super(sessionId);
        this.guestSessionId = guestSessionId;
    }

    public String getGuestSessionId() {
        return guestSessionId;
    }


    @Override
    public String toString() {
        return "BeforeSessionCreatedEvent{" +
                "guestSessionId='" + guestSessionId + '\'' +
                "} " + super.toString();
    }
}
