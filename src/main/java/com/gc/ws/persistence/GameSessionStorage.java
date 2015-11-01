package com.gc.ws.persistence;

import com.gc.ws.domain.GameSession;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/29/2015
 */
@Component
public class GameSessionStorage {

    private Map<String, GameSession> gameSessions = new HashMap<>();

    public GameSession save(GameSession gameSession) {
        return gameSessions.put(gameSession.getId(), gameSession);
    }

    public GameSession get(String id) {
        return gameSessions.get(id);
    }


    public void remove(String id) {
        gameSessions.remove(id);
    }

    public List<GameSession> findByUserSessionId(String sessionId) {
        return gameSessions.values()
                .stream()
                .filter(session -> session.getOwner().getSessionId().equals(sessionId) || session.getGuest().getSessionId().equals(sessionId))
                .collect(Collectors.toList());
    }

    public boolean existsForUsers(String sessionId1, String sessionId2) {
        return gameSessions.values()
                .stream()
                .anyMatch(session -> session.belongsToUsers(sessionId1, sessionId2));

    }
}
