package com.gc.ws.events;

import com.gc.ws.User;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
public class UserLoginCompleteEvent extends Event {

    public User user;

    public UserLoginCompleteEvent(String sessionId, User user) {
        super(sessionId);
        this.user = user;
    }


    @Override
    public String toString() {
        return "UserLoginCompleteEvent{" +
                "user=" + user +
                "} " + super.toString();
    }
}
