package com.gc.ws.events;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
public class GameSessionCloseRequestEvent extends Event {

    private String gameSessionId;

    public GameSessionCloseRequestEvent(String sessionId, String gameSessionId) {
        super(sessionId);
        this.gameSessionId = gameSessionId;
    }

    public String getGameSessionId() {
        return gameSessionId;
    }

    @Override
    public String toString() {
        return "GameSessionCloseRequestEvent{" +
                "gameSessionId='" + gameSessionId + '\'' +
                "} " + super.toString();
    }
}
