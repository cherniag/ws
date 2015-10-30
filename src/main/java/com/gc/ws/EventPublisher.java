package com.gc.ws;

import com.gc.ws.events.Event;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
@Component
public class EventPublisher {
    private Map<Class, List<EventListener>> listenerMap = new HashMap<>();

    public void publish(Event event) {
        List<EventListener> listeners = listenerMap.get(event.getClass());
        if (listeners != null) {
            listeners.forEach(eventListener -> eventListener.onEvent(event));
        }
    }

    public void addListener(Class eventType, EventListener listener) {
        if (!listenerMap.containsKey(eventType)) {
            listenerMap.put(eventType, new ArrayList<>());
        }
        listenerMap.get(eventType).add(listener);
    }


}
