package com.gc.ws;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/29/2015
 */
public class GameSession {
    private final String id;
    private final User holder;
    private final User guest;

    public GameSession(User holder, User guest) {
        this.holder = holder;
        this.guest = guest;
        id = holder.getSessionId() + "_" + guest.getSessionId();
    }

    public String getId() {
        return id;
    }

    public String getOppositeId(String sessionId) {
        if (holder.getSessionId().equals(sessionId)) {
            return guest.getSessionId();
        } else if (guest.getSessionId().equals(sessionId)) {
            return holder.getSessionId();
        }
        return null;
    }

    public User getHolder() {
        return holder;
    }

    public User getGuest() {
        return guest;
    }
}
