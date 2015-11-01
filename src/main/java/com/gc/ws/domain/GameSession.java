package com.gc.ws.domain;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/29/2015
 */
public class GameSession {
    private final String id;
    private final User owner;
    private final User guest;
    private GameSessionStatus status;

    public GameSession(User owner, User guest) {
        this.owner = owner;
        this.guest = guest;
        id = owner.getSessionId() + "_" + guest.getSessionId();
        status = GameSessionStatus.INITIATED;
    }

    public String getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public User getGuest() {
        return guest;
    }

    public GameSessionStatus getStatus() {
        return status;
    }

    public void accept() {
        status = GameSessionStatus.ACCEPTED;
    }

    public boolean belongsToUsers(String userSessionId1, String userSessionId2) {
        return owner.getSessionId().equals(userSessionId1) && guest.getSessionId().equals(userSessionId2) ||
                guest.getSessionId().equals(userSessionId1) && owner.getSessionId().equals(userSessionId2);
    }

    @Override
    public String toString() {
        return "GameSession{" +
                "id='" + id + '\'' +
                ", owner=" + owner +
                ", guest=" + guest +
                ", status=" + status +
                '}';
    }
}
