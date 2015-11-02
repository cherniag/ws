package com.gc.ws.service;

import com.gc.ws.domain.User;
import com.gc.ws.events.*;
import com.gc.ws.events.user.*;
import com.gc.ws.persistence.UserStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.stream.Collectors;

import static com.gc.ws.transport.MessageType.USER_LIST;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/29/2015
 */
@Service
public class UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);
    @Resource
    private UserStorage userStorage;
    @Resource
    private EventPublisher eventPublisher;

    @PostConstruct
    public void init() {
        registerInterestedEventListeners();
    }

    private void login(String userName, String sessionId) {
        logger.debug("Login {} sessionId {}", userName, sessionId);
        if (userStorage.exists(userName)) {
            eventPublisher.publish(new UserErrorEvent(sessionId, "Duplicate user name"));
            return;
        }
        if (userName.trim().isEmpty()) {
            eventPublisher.publish(new UserErrorEvent(sessionId, "Wrong user name"));
            return;
        }
        User user = new User(userName, sessionId);
        userStorage.add(sessionId, user);
        eventPublisher.publish(new AfterUserLoginEvent(sessionId, user));
        eventPublisher.publish(new UserMessageEvent(sessionId, USER_LIST, getOtherUsersOnline(userName)));
    }

    private void logout(String sessionId) {
        logger.debug("Logout sessionId {}", sessionId);
        User removed = userStorage.remove(sessionId);
        eventPublisher.publish(new AfterUserLogoutEvent(sessionId, removed));
    }


    private Collection<User> getOtherUsersOnline(String userName) {
        Collection<User> users = userStorage.getAll();
        return users.stream().filter(user -> !user.getUserName().equals(userName)).collect(Collectors.toList());
    }

    private void registerInterestedEventListeners() {
        eventPublisher.addListener(ConnectionClosedEvent.class, new EventListener<ConnectionClosedEvent>() {
            @Override
            public void onEvent(ConnectionClosedEvent event) {
                logout(event.sessionId);
            }
        });

        eventPublisher.addListener(UserLoginEvent.class, new EventListener<UserLoginEvent>() {
            @Override
            public void onEvent(UserLoginEvent event) {
                login(event.userName, event.sessionId);
            }
        });
    }
}
