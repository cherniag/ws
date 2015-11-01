package com.gc.ws.persistence;

import com.gc.ws.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/29/2015
 */
@Component
public class UserStorage {
    private Map<String, User> users = new ConcurrentHashMap<>();

    public void add(String sessionId, User user) {
        users.put(sessionId, user);
    }

    public User get(String sessionId) {
        return users.get(sessionId);
    }

    public User remove(String sessionId) {
        return users.remove(sessionId);
    }

    public boolean exists(String userName) {
        return users.entrySet().stream().anyMatch(stringUserEntry -> stringUserEntry.getValue().getUserName().equals(userName));
    }

    public Collection<User> getAll() {
        return users.values();
    }

}
