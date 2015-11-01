package com.gc.ws.events.user;

import com.gc.ws.domain.User;
import com.gc.ws.events.Event;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
public class AfterUserLoginEvent extends Event {

    public User user;

    public AfterUserLoginEvent(String sessionId, User user) {
        super(sessionId);
        this.user = user;
    }


    @Override
    public String toString() {
        return "AfterUserLoginEvent{" +
                "user=" + user +
                "} " + super.toString();
    }
}
