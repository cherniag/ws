package com.gc.ws.events;

/**
 * Created by gc on 01.11.2015.
 */
public interface EventPublisher {

    void publish(Event event);

    void addListener(Class eventType, EventListener listener);

}
