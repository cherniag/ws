package com.gc.ws.events.gamesession;

import com.gc.ws.domain.GameSession;
import com.gc.ws.events.Event;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
public class AfterGameSessionInitEvent extends Event {
    private GameSession gameSession;

    public AfterGameSessionInitEvent(GameSession gameSession) {
        super(null);

        this.gameSession = gameSession;
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    @Override
    public String toString() {
        return "AfterGameSessionInitEvent{" +
                "gameSession=" + gameSession +
                "} " + super.toString();
    }
}
