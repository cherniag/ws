package com.gc.ws.events;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
public interface EventListener<T extends Event> {

    void onEvent(T event);
}
