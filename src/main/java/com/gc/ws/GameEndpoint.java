package com.gc.ws;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/29/2015
 */
@ServerEndpoint("/lobby")
public class GameEndpoint {
    private ConnectionManager connectionManager;

    public GameEndpoint() {
        connectionManager = SpringContextHolder.getApplicationContext().getBean("connectionManager", ConnectionManager.class);
    }

    @OnOpen
    public void open(Session session) {
        connectionManager.open(session);
    }

    @OnError
    public void error(Session session, Throwable t) {
        connectionManager.error(session, t);
    }

    @OnClose
    public void closedConnection(Session session) {
        connectionManager.closedConnection(session);
    }

    @OnMessage
    public void onMessage(Session session, String msg) {
        connectionManager.onMessage(session.getId(), msg);
    }




}
