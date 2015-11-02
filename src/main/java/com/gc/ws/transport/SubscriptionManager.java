package com.gc.ws.transport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
@Component
class SubscriptionManager {
    private static final Logger logger = LogManager.getLogger(SubscriptionManager.class);
    private Map<MessageType, Set<String>> subscribers = new HashMap<>();

    Set<String> getSubscribers(MessageType topic) {
        Set<String> s = subscribers.get(topic);
        return s == null ? Collections.emptySet() : s;
    }

    void subscribe(String sessionId, MessageType... topics) {
        logger.debug("Subscribe {} to {}", sessionId, topics);
        for (MessageType topic : topics) {
            if(!subscribers.containsKey(topic)) {
                subscribers.put(topic, new HashSet<>());
            }
            subscribers.get(topic).add(sessionId);
        }
    }

    void unsubscribe(String sessionId) {
        logger.debug("Unsubscribe {}", sessionId);
        subscribers.entrySet().forEach(responseTypeSetEntry -> {
            responseTypeSetEntry.getValue().remove(sessionId);
        });
    }
}
