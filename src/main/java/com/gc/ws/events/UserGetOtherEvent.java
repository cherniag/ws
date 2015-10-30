package com.gc.ws.events;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
public class UserGetOtherEvent extends Event {

    private Object payload;

    public UserGetOtherEvent(String sessionId, Object payload) {
        super(sessionId);
        this.payload = payload;
    }

    public Object getPayload() {
        return payload;
    }


    @Override
    public String toString() {
        return "UserGetOtherEvent{" +
                "payload=" + payload +
                "} " + super.toString();
    }
}
