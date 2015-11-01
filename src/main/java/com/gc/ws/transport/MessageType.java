package com.gc.ws.transport;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/29/2015
 */
public enum MessageType {
    USER_TRY_LOGIN, USER_LIST, USER_LOGGED_IN, USER_LOGGED_OUT, USER_ERROR,
    GAME_SESSION_INIT, GAME_SESSION_ACCEPTED, GAME_SESSION_CREATED, GAME_SESSION_REJECTED, GAME_SESSION_CLOSE,
    GAME_SESSION_REMOVED;

    private static Set<MessageType> usualSubscriptions = new HashSet<>(Arrays.asList(USER_LOGGED_IN, USER_LOGGED_OUT));

    public static Set<MessageType> usualSubscription() {
        return Collections.unmodifiableSet(usualSubscriptions);
    }
}
