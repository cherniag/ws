package com.gc.ws;

import com.gc.ws.events.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/29/2015
 */
@Service
public class GameService {
    @Resource
    private OnlineUsersStorage onlineUsersStorage;
    @Resource
    private EventPublisher eventPublisher;

    private Map<String, GameSession> gameSessions = new HashMap<>();

    @PostConstruct
    public void init() {
        eventPublisher.addListener(SessionClosedEvent.class, new EventListener<SessionClosedEvent>() {
            @Override
            public void onEvent(SessionClosedEvent event) {
                System.out.println(event);
                User removed = logout(event.sessionId);
                eventPublisher.publish(new UserLogoutCompleteEvent(event.sessionId, removed));

                List<GameSession> found = gameSessions.values().stream().filter(session -> session.getId().contains(event.sessionId)).collect(Collectors.toList());

                if (!found.isEmpty()) {
                    found.stream().forEach(session -> {
                        eventPublisher.publish(new GameSessionClosedEvent(session.getOppositeId(event.sessionId),session));
                        gameSessions.remove(session.getId());
                    });

                }

            }
        });

        eventPublisher.addListener(GameSessionCloseRequestEvent.class, new EventListener<GameSessionCloseRequestEvent>() {
            @Override
            public void onEvent(GameSessionCloseRequestEvent event) {
                System.out.println(event);
                GameSession gameSession = gameSessions.get(event.getGameSessionId());
                if (gameSession != null) {
                    eventPublisher.publish(new GameSessionClosedEvent(gameSession.getOppositeId(event.sessionId), gameSession));
                    eventPublisher.publish(new GameSessionClosedEvent(event.sessionId, gameSession));
                    gameSessions.remove(event.getGameSessionId());
                }
            }
        });

        eventPublisher.addListener(LoginEvent.class, new EventListener<LoginEvent>() {
            @Override
            public void onEvent(LoginEvent event) {
                System.out.println(event);
                try {
                    User user = login(event.userName, event.sessionId);
                    eventPublisher.publish(new UserLoginCompleteEvent(event.sessionId, user));
                    eventPublisher.publish(new UserGetOtherEvent(event.sessionId, getUsersOnline(event.userName)));
                } catch (ServiceException e) {
                    eventPublisher.publish(new UserErrorEvent(event.sessionId, e.getMessage()));
                }

            }
        });

        eventPublisher.addListener(BeforeSessionCreatedEvent.class, new EventListener<BeforeSessionCreatedEvent>() {
            @Override
            public void onEvent(BeforeSessionCreatedEvent event) {
                System.out.println(event);
                boolean alreadyExist = gameSessions.values().stream().anyMatch(session -> session.getId().contains(event.getGuestSessionId()) && session.getId().contains(event.sessionId));
                if (alreadyExist) {
                    eventPublisher.publish(new UserErrorEvent(event.sessionId, "Session with " + onlineUsersStorage.get(event.getGuestSessionId()).getUserName() + " already exists!"));
                    return;
                }

                User initiator = onlineUsersStorage.get(event.sessionId);
                User guest = onlineUsersStorage.get(event.getGuestSessionId());
                GameSession gameSession = new GameSession(initiator, guest);
                gameSessions.put(gameSession.getId(), gameSession);
                eventPublisher.publish(new AfterSessionCreatedEvent(event.sessionId, gameSession));
            }
        });
    }

    public User login(String userName, String sessionId) throws ServiceException {
        if (onlineUsersStorage.exists(userName)) {
            throw new ServiceException("Duplicate user name");
        }
        User user = new User(userName, sessionId);
        onlineUsersStorage.add(sessionId, user);
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
