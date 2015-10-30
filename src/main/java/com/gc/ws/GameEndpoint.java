package com.gc.ws;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/29/2015
 */
@ServerEndpoint("/lobby")
public class GameEndpoint {
    private GameService gameService = new GameService();
    private MessageConverter messageConverter = new MessageConverter();
    private Set<Session> connected = new HashSet<>();

    @OnOpen
    public void open(Session session) {
        System.out.println("open " + session.getId());
        requestAuthorization(session);
    }

    @OnError
    public void error(Session session, Throwable t) {
        System.err.println("Error on session " + session.getId() + " t " + t);
        // ???
    }

    @OnClose
    public void closedConnection(Session session) {
        System.out.println("session closed: " + session.getId());
        User user = gameService.logout(session.getId());
        connected.remove(session);
        userLoggedOut(user);
    }

    @OnMessage
    public void onMessage(Session session, String msg) {
        try {
            System.out.println("received msg " + msg + " from " + session.getId());
            ClientRequest clientRequest = messageConverter.convert(msg);
            switch (clientRequest.type) {
                case LOGIN:
                    String userName = (String) clientRequest.payload;
                    User user = gameService.login(userName, session);
                    userLoggedIn(user);
                    connected.add(session);
                    sendUsersOnline(session, gameService.getUsersOnline(user.getUserName()));

            }


        } catch (ServiceException e) {
            try {
                session.getBasicRemote().sendText(messageConverter.convert(new ServerResponse(ResponseType.ERROR, e.getMessage())));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestAuthorization(Session session) {
        try {
            session.getBasicRemote().sendText(messageConverter.convert(new ServerResponse(ResponseType.LOGIN_REQUEST, null)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendUsersOnline(Session session, Collection<User> usersOnline) {
        try {
            session.getBasicRemote().sendText(messageConverter.convert(new ServerResponse(ResponseType.USER_LIST, usersOnline)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void userLoggedIn(User user) {
        ServerResponse response = new ServerResponse(ResponseType.USER_LOGGED_IN, new User[]{user});
        connected.stream().filter(Session::isOpen).forEach(
                session -> {
                    try {
                        session.getBasicRemote().sendText(messageConverter.convert(response));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        // process closed and notify
    }

    private void userLoggedOut(User user) {
        ServerResponse response = new ServerResponse(ResponseType.USER_LOGGED_OUT, new User[]{user});
        connected.stream().forEach(
                session -> {
                    try {
                        session.getBasicRemote().sendText(messageConverter.convert(response));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        // process closed and notify
    }


}
