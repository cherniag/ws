package com.gc.ws.events;

import com.gc.ws.GameSession;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
public class AfterSessionCreatedEvent extends Event {
    private GameSession gameSession;

    public AfterSessionCreatedEvent(String sessionId, GameSession gameSession) {
        super(sessionId);

        this.gameSession = gameSession;
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    @Override
    public String toString() {
        return "AfterSessionCreatedEvent{" +
                "gameSession=" + gameSession +
                "} " + super.toString();
    }
}
