package com.gc.ws.events;

import com.gc.ws.User;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
public class AfterSessionCreatedEvent extends Event {
    private User holder;
    private User guest;

    public AfterSessionCreatedEvent(String sessionId, User holder, User guest) {
        super(sessionId);
        this.holder = holder;
        this.guest = guest;
    }

    public User getGuest() {
        return guest;
    }

    public User getHolder() {
        return holder;
    }


    @Override
    public String toString() {
        return "AfterSessionCreatedEvent{" +
                "holder=" + holder +
                ", guest=" + guest +
                "} " + super.toString();
    }
}
