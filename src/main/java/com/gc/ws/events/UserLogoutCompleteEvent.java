package com.gc.ws.events;

import com.gc.ws.User;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
public class UserLogoutCompleteEvent extends Event {

    public User user;

    public UserLogoutCompleteEvent(String sessionId, User user) {
        super(sessionId);
        this.user = user;
    }


    @Override
    public String toString() {
        return "UserLogoutCompleteEvent{" +
                "user=" + user +
                "} " + super.toString();
    }
}
