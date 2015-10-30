package com.gc.ws;

import com.gc.ws.events.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.websocket.Session;
import java.io.IOException;
import java.util.*;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
@Component
public class ConnectionManager {
    private Map<String, Session> connected = new HashMap<>();
    private Map<ClientRequestType, MessageHandler> clientRequestTypeMessageHandlerMap = new HashMap<>();
    @Resource
    private SubscriptionManager subscriptionManager;
    @Resource
    private MessageConverter messageConverter;
    @Resource
    private EventPublisher eventPublisher;


    @PostConstruct
    public void init() {
        eventPublisher.addListener(UserLoginCompleteEvent.class, new EventListener<UserLoginCompleteEvent>(){
            @Override
            public void onEvent(UserLoginCompleteEvent event) {
                System.out.println(event);
                broadcast(new ServerResponse(ResponseType.USER_LOGGED_IN, new User[]{event.user}));
            }
        });

        eventPublisher.addListener(UserErrorEvent.class, new EventListener<UserErrorEvent>(){
            @Override
            public void onEvent(UserErrorEvent event) {
                System.out.println(event);
                send(event.sessionId, new ServerResponse(ResponseType.ERROR, event.getError()));
            }
        });

        eventPublisher.addListener(UserGetOtherEvent.class, new EventListener<UserGetOtherEvent>(){
            @Override
            public void onEvent(UserGetOtherEvent event) {
                System.out.println(event);
                send(event.sessionId, new ServerResponse(ResponseType.USER_LIST, event.getPayload()));
            }
        });

        eventPublisher.addListener(UserLogoutCompleteEvent.class, new EventListener<UserLogoutCompleteEvent>(){
            @Override
            public void onEvent(UserLogoutCompleteEvent event) {
                System.out.println(event);
                broadcast(new ServerResponse(ResponseType.USER_LOGGED_OUT, new User[]{event.user}));
            }
        });

        eventPublisher.addListener(AfterSessionCreatedEvent.class, new EventListener<AfterSessionCreatedEvent>(){
            @Override
            public void onEvent(AfterSessionCreatedEvent event) {
                System.out.println(event);
                send(event.getHolder().getSessionId(), new ServerResponse(ResponseType.SESSION_CREATED, event.getGuest()));
                send(event.getGuest().getSessionId(), new ServerResponse(ResponseType.SESSION_CREATED, event.getHolder()));
            }
        });

        eventPublisher.addListener(GameSessionClosedEvent.class, new EventListener<GameSessionClosedEvent>(){
            @Override
            public void onEvent(GameSessionClosedEvent event) {
                System.out.println(event);
                send(event.sessionId, new ServerResponse(ResponseType.SESSION_CLOSED, event.getClosedUser()));
            }
        });


        clientRequestTypeMessageHandlerMap.put(ClientRequestType.LOGIN, (from, clientRequest) -> {
            eventPublisher.publish(new LoginEvent(from, (String) clientRequest.payload));
        });

        clientRequestTypeMessageHandlerMap.put(ClientRequestType.SESSION_REQUEST, (from, clientRequest) -> {
            send((String) clientRequest.payload, new ServerResponse(ResponseType.SESSION_REQUEST, from));
        });

        clientRequestTypeMessageHandlerMap.put(ClientRequestType.SESSION_REQUEST_CONFIRM, (from, clientRequest) -> {
            eventPublisher.publish(new BeforeSessionCreatedEvent((String) clientRequest.payload, from));
        });

        clientRequestTypeMessageHandlerMap.put(ClientRequestType.SESSION_REQUEST_REJECT, (from, clientRequest) -> {
            send((String) clientRequest.payload, new ServerResponse(ResponseType.SESSION_REJECT, from));
        });
    }

    public void open(Session session) {
        connected.put(session.getId(), session);
    }

    public void error(Session session, Throwable t) {
        connected.remove(session.getId());
        eventPublisher.publish(new SessionClosedEvent(session.getId()));
    }

    public void closedConnection(Session session) {
        connected.remove(session.getId());
        eventPublisher.publish(new SessionClosedEvent(session.getId()));
    }

    public void onMessage(String from, String msg) {
        ClientRequest clientRequest = messageConverter.convert(msg);
        MessageHandler messageHandler = clientRequestTypeMessageHandlerMap.get(clientRequest.type);
        if (messageHandler != null) {
            messageHandler.handle(from, clientRequest);
        }
    }

    public void send(String to, ServerResponse serverResponse) {
        Session recipient = connected.get(to);
        if (recipient != null) {
            try {
                recipient.getBasicRemote().sendText(messageConverter.convert(serverResponse));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcast(ServerResponse serverResponse) {
        Set<String> recipients = subscriptionManager.getSubscribers(serverResponse.responseType);
        recipients.stream().forEach(s -> {
            try {
                connected.get(s).getBasicRemote().sendText(messageConverter.convert(serverResponse));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }



}
