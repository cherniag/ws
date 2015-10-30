package com.gc.ws.events;

import com.gc.ws.GameSession;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
public class GameSessionClosedEvent extends Event {

    private GameSession gameSession;

    public GameSessionClosedEvent(String sessionId, GameSession gameSession) {
        super(sessionId);
        this.gameSession = gameSession;
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    @Override
    public String toString() {
        return "GameSessionClosedEvent{" +
                "gameSession=" + gameSession +
                "} " + super.toString();
    }
}
