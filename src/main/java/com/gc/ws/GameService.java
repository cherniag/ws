package com.gc.ws;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/29/2015
 */
@Service
public class GameService {
    @Resource
    private OnlineUsersStorage onlineUsersStorage;

    public User login(String userName, Session session) throws ServiceException {
        if (onlineUsersStorage.exists(userName)) {
            throw new ServiceException("Duplicate user name");
        }
        User user = new User(userName, session.getId());
        onlineUsersStorage.add(session.getId(), user);
        return user;
    }


    public User logout(String sessionId) {
        User removed = onlineUsersStorage.remove(sessionId);
        return removed;
    }

    public Collection<User> getUsersOnline(String userName) {
        Collection<User> users = onlineUsersStorage.getAll();
        return users.stream().filter(user -> !user.getUserName().equals(userName)).collect(Collectors.toList());
    }
}
