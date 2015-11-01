package com.gc.ws.events.gamesession;

import com.gc.ws.domain.GameSession;
import com.gc.ws.events.Event;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
public class AfterGameSessionRejectEvent extends Event {
    private GameSession gameSession;

    public AfterGameSessionRejectEvent(String sessionId, GameSession gameSession) {
        super(sessionId);

        this.gameSession = gameSession;
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    @Override
    public String toString() {
        return "AfterGameSessionRejectEvent{" +
                "gameSession=" + gameSession +
                "} " + super.toString();
    }
}
