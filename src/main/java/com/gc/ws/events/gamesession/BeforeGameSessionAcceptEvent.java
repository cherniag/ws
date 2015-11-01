package com.gc.ws.events.gamesession;

import com.gc.ws.events.Event;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
public class BeforeGameSessionAcceptEvent extends Event {
    private String gameSessionId;

    public BeforeGameSessionAcceptEvent(String sessionId, String gameSessionId) {
        super(sessionId);
        this.gameSessionId = gameSessionId;
    }

    public String getGameSessionId() {
        return gameSessionId;
    }

    @Override
    public String toString() {
        return "BeforeGameSessionAcceptEvent{" +
                "gameSessionId='" + gameSessionId + '\'' +
                "} " + super.toString();
    }

}
