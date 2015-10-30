package com.gc.ws.events;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
public class UserErrorEvent extends Event {

    private String error;

    public UserErrorEvent(String sessionId, String error) {
        super(sessionId);
        this.error = error;
    }

    public String getError() {
        return error;
    }


    @Override
    public String toString() {
        return "UserErrorEvent{" +
                "error='" + error + '\'' +
                "} " + super.toString();
    }
}
