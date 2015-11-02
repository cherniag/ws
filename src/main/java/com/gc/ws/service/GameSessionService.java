package com.gc.ws.service;

import com.gc.ws.domain.GameSession;
import com.gc.ws.domain.User;
import com.gc.ws.events.*;
import com.gc.ws.events.gamesession.*;
import com.gc.ws.events.user.UserErrorEvent;
import com.gc.ws.persistence.GameSessionStorage;
import com.gc.ws.persistence.UserStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * Created by gc on 01.11.2015.
 */
@Service
public class GameSessionService {
    private static final Logger logger = LogManager.getLogger(GameSessionService.class);
    @Resource
    private EventPublisher eventPublisher;
    @Resource
    private GameSessionStorage gameSessionStorage;
    @Resource
    private UserStorage userStorage;

    @PostConstruct
    public void init() {
        registerInterestedListeners();
    }

    void initGameSession(String ownerSessionId, String guestSessionId) {
        logger.debug("Init game session owner {} guest {}", ownerSessionId, guestSessionId);
        if (gameSessionStorage.existsForUsers(ownerSessionId, guestSessionId)) {
            eventPublisher.publish(new UserErrorEvent(ownerSessionId, "Session with " + userStorage.get(guestSessionId).getUserName() + " already exists!"));
            return;
        }

        User holder = userStorage.get(ownerSessionId);
        User guest = userStorage.get(guestSessionId);
        GameSession gameSession = new GameSession(holder, guest);
        gameSessionStorage.save(gameSession);
        eventPublisher.publish(new AfterGameSessionInitEvent(gameSession));
    }

    void acceptGameSession(String userSessionId, String gameSessionId) {
        logger.debug("Accept game session user {} game session id {}", userSessionId, gameSessionId);
        GameSession gameSession = gameSessionStorage.get(gameSessionId);
        if (gameSession == null) {
            eventPublisher.publish(new UserErrorEvent(userSessionId, "Session with " + gameSessionId + " doesn't exist!"));
            return;
        }
        gameSession.accept();
        gameSessionStorage.save(gameSession);
        eventPublisher.publish(new AfterGameSessionAcceptEvent(gameSession));
    }

    void rejectGameSession(String userSessionId, String gameSessionId) {
        logger.debug("Reject game session user {} game session id {}", userSessionId, gameSessionId);
        GameSession gameSession = gameSessionStorage.get(gameSessionId);
        if (gameSession == null) {
            eventPublisher.publish(new UserErrorEvent(userSessionId, "Session with " + gameSessionId + " doesn't exist!"));
            return;
        }
        gameSessionStorage.remove(gameSessionId);
        eventPublisher.publish(new AfterGameSessionRejectEvent(userSessionId, gameSession));
    }

    void closeGameSession(String userSessionId, String gameSessionId) {
        logger.debug("Close game session user {} game session id {}", userSessionId, gameSessionId);
        GameSession found = gameSessionStorage.get(gameSessionId);

        if (found==null) {
            eventPublisher.publish(new UserErrorEvent(userSessionId, "Session with " + gameSessionId + " doesn't exist!"));
            return;
        }
        gameSessionStorage.remove(gameSessionId);
        eventPublisher.publish(new AfterGameSessionCloseEvent(found));
    }

    void closeUserGameSessions(String userSessionId) {
        logger.debug("Close all game sessions  for user {}", userSessionId);
        List<GameSession> found = gameSessionStorage.findByUserSessionId(userSessionId);

        if (!found.isEmpty()) {
            found.stream().forEach(session -> {
                gameSessionStorage.remove(session.getId());
                eventPublisher.publish(new AfterGameSessionCloseEvent(session));
            });
        }
    }

    private void registerInterestedListeners() {
        eventPublisher.addListener(BeforeGameSessionInitEvent.class, new EventListener<BeforeGameSessionInitEvent>() {
            @Override
            public void onEvent(BeforeGameSessionInitEvent event) {
                initGameSession(event.sessionId, event.getGuestSessionId());
            }
        });

        eventPublisher.addListener(BeforeGameSessionAcceptEvent.class, new EventListener<BeforeGameSessionAcceptEvent>() {
            @Override
            public void onEvent(BeforeGameSessionAcceptEvent event) {
                acceptGameSession(event.sessionId, event.getGameSessionId());
            }
        });

        eventPublisher.addListener(BeforeGameSessionRejectEvent.class, new EventListener<BeforeGameSessionRejectEvent>() {
            @Override
            public void onEvent(BeforeGameSessionRejectEvent event) {
                rejectGameSession(event.sessionId, event.getGameSessionId());
            }
        });

        eventPublisher.addListener(ConnectionClosedEvent.class, new EventListener<ConnectionClosedEvent>() {
            @Override
            public void onEvent(ConnectionClosedEvent event) {
                closeUserGameSessions(event.sessionId);
            }
        });

        eventPublisher.addListener(BeforeGameSessionCloseEvent.class, new EventListener<BeforeGameSessionCloseEvent>() {
            @Override
            public void onEvent(BeforeGameSessionCloseEvent event) {
                closeGameSession(event.sessionId, event.getGameSessionId());
            }
        });
    }
}
