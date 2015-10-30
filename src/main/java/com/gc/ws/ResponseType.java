package com.gc.ws;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/29/2015
 */
public enum ResponseType {
    USER_LIST, USER_LOGGED_IN, USER_LOGGED_OUT, ERROR, SESSION_CREATED, SESSION_REQUEST, SESSION_REJECT, SESSION_CLOSED;

    private static Set<ResponseType> usualSubscriptions = new HashSet<>(Arrays.asList(USER_LOGGED_IN, USER_LOGGED_OUT));

    public static Set<ResponseType> usualSubscription() {
        return Collections.unmodifiableSet(usualSubscriptions);
    }
}
