package com.gc.ws.events.user;

import com.gc.ws.events.Event;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
public class UserLoginEvent extends Event {

    public String userName;

    public UserLoginEvent(String sessionId, String userName) {
        super(sessionId);
        this.userName = userName;
    }


    @Override
    public String toString() {
        return "UserLoginEvent{" +
                "userName='" + userName + '\'' +
                "} " + super.toString();
    }
}
