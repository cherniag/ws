package com.gc.ws.events.gamesession;

import com.gc.ws.events.Event;

/**
 * Created by gc on 01.11.2015.
 */
public class BeforeGameSessionInitEvent extends Event {
    private String guestSessionId;

    public BeforeGameSessionInitEvent(String sessionId, String guestSessionId) {
        super(sessionId);
        this.guestSessionId = guestSessionId;
    }

    public String getGuestSessionId() {
        return guestSessionId;
    }

    @Override
    public String toString() {
        return "BeforeGameSessionInitEvent{" +
                ", guestSessionId='" + guestSessionId + '\'' +
                '}';
    }
}
