package com.gc.ws.events;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
public class LoginEvent extends Event {

    public String userName;

    public LoginEvent(String sessionId, String userName) {
        super(sessionId);
        this.userName = userName;
    }


    @Override
    public String toString() {
        return "LoginEvent{" +
                "userName='" + userName + '\'' +
                "} " + super.toString();
    }
}
