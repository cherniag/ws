package com.gc.ws.events.gamesession;

import com.gc.ws.events.Event;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
public class BeforeGameSessionCloseEvent extends Event {

    private String gameSessionId;

    public BeforeGameSessionCloseEvent(String sessionId, String gameSessionId) {
        super(sessionId);
        this.gameSessionId = gameSessionId;
    }

    public String getGameSessionId() {
        return gameSessionId;
    }

    @Override
    public String toString() {
        return "BeforeGameSessionCloseEvent{" +
                "gameSessionId='" + gameSessionId + '\'' +
                "} " + super.toString();
    }
}
