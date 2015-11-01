package com.gc.ws.events.user;

import com.gc.ws.domain.User;
import com.gc.ws.events.Event;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
public class AfterUserLogoutEvent extends Event {

    public User user;

    public AfterUserLogoutEvent(String sessionId, User user) {
        super(sessionId);
        this.user = user;
    }


    @Override
    public String toString() {
        return "AfterUserLogoutEvent{" +
                "user=" + user +
                "} " + super.toString();
    }
}
