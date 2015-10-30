package com.gc.ws;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/29/2015
 */
public class User {

    private final String userName;
    private final String sessionId;

    public User(String userName, String sessionId) {
        this.userName = userName;
        this.sessionId = sessionId;
    }

    public String getUserName() {
        return userName;
    }

    public String getSessionId() {
        return sessionId;
    }

    @Override
    public String toString() {
        return "User{userName='" + userName + "\'}";
    }
}
