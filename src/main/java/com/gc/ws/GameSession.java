package com.gc.ws;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/29/2015
 */
public class GameSession {
    private final User holder;
    private final User guest;

    public GameSession(User holder, User guest) {
        this.holder = holder;
        this.guest = guest;
    }


    public User getHolder() {
        return holder;
    }

    public User getGuest() {
        return guest;
    }
}
