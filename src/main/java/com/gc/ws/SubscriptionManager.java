package com.gc.ws;

import com.gc.ws.events.UserLoginCompleteEvent;
import com.gc.ws.events.UserLogoutCompleteEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
@Component
public class SubscriptionManager {
    @Resource
    private EventPublisher eventPublisher;

    private Map<ResponseType, Set<String>> subscribers = new HashMap<>();

    @PostConstruct
    public void init() {
        eventPublisher.addListener(UserLoginCompleteEvent.class, new EventListener<UserLoginCompleteEvent>(){
            @Override
            public void onEvent(UserLoginCompleteEvent event) {
                System.out.println(event);
                subscribe(ResponseType.usualSubscription(), event.sessionId);
            }
        });

        eventPublisher.addListener(UserLogoutCompleteEvent.class, new EventListener<UserLogoutCompleteEvent>(){
            @Override
            public void onEvent(UserLogoutCompleteEvent event) {
                System.out.println(event);
                unsubscribe(event.sessionId);
            }
        });
    }

    public Set<String> getSubscribers(ResponseType topic) {
        Set<String> s = subscribers.get(topic);
        return s == null ? Collections.emptySet() : s;
    }

    public void subscribe(Set<ResponseType> topics, String sessionId) {
        topics.forEach(topic -> {
            if(!subscribers.containsKey(topic)) {
                subscribers.put(topic, new HashSet<>());
            }
            subscribers.get(topic).add(sessionId);
        });
    }

    public void unsubscribe(String sessionId) {
        subscribers.entrySet().forEach(responseTypeSetEntry -> {
            responseTypeSetEntry.getValue().remove(sessionId);
        });
    }
}
