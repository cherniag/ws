package com.gc.ws.events;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
@Component
public class SynchronousEventPublisher implements EventPublisher {
    private static final Logger logger = LogManager.getLogger(SynchronousEventPublisher.class);
    private Map<Class, List<EventListener>> listenerMap = new HashMap<>();

    @Override
    public void publish(Event event) {
        logger.debug("Publish event {}", event);
        List<EventListener> listeners = listenerMap.get(event.getClass());
        if (listeners != null) {
            listeners.forEach(eventListener -> eventListener.onEvent(event));
        }
    }

    @Override
    public void addListener(Class eventType, EventListener listener) {
        logger.debug("Add listener {} for {}", listener, eventType);
        if (!listenerMap.containsKey(eventType)) {
            listenerMap.put(eventType, new ArrayList<>());
        }
        listenerMap.get(eventType).add(listener);
    }


}
