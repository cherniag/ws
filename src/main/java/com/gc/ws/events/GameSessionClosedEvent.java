package com.gc.ws.events;

import com.gc.ws.User;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
public class GameSessionClosedEvent extends Event {

    private User closedUser;

    public GameSessionClosedEvent(String sessionId, User closedUser) {
        super(sessionId);
        this.closedUser = closedUser;
    }

    public User getClosedUser() {
        return closedUser;
    }

    @Override
    public String toString() {
        return "GameSessionClosedEvent{" +
                "closedUser=" + closedUser +
                "} " + super.toString();
    }
}
