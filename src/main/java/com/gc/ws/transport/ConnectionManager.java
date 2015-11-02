package com.gc.ws.transport;

import com.gc.ws.domain.GameSession;
import com.gc.ws.domain.User;
import com.gc.ws.events.ConnectionClosedEvent;
import com.gc.ws.events.EventListener;
import com.gc.ws.events.EventPublisher;
import com.gc.ws.events.gamesession.*;
import com.gc.ws.events.user.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.gc.ws.transport.MessageType.*;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
@Component
class ConnectionManager {
    private static final Logger logger = LogManager.getLogger(ConnectionManager.class);
    private Map<String, Session> connections = new HashMap<>();
    private Map<MessageType, ClientMessageHandler> clientMessageHandlerMap = new HashMap<>();
    @Resource
    private SubscriptionManager subscriptionManager;
    @Resource
    private MessageConverter messageConverter;
    @Resource
    private EventPublisher eventPublisher;


    @PostConstruct
    public void init() {
        registerInterestedEventListeners();
        initClientMessageHandlers();
    }

    void open(Session session) {
        logger.debug("Open session {}", session);
        connections.put(session.getId(), session);
    }

    void error(Session session, Throwable t) {
        logger.warn("Error on session {}", session, t);
        connections.remove(session.getId());
        eventPublisher.publish(new ConnectionClosedEvent(session.getId()));
    }

    void closedConnection(Session session) {
        logger.debug("Close session {}", session);
        connections.remove(session.getId());
        eventPublisher.publish(new ConnectionClosedEvent(session.getId()));
    }

    void onMessage(String from, String msg) {
        logger.debug("OnMessage session {}, message {}", from, msg);
        Message clientMessage = messageConverter.convert(msg);
        ClientMessageHandler clientMessageHandler = clientMessageHandlerMap.get(clientMessage.messageType);
        if (clientMessageHandler != null) {
            clientMessageHandler.handle(from, clientMessage);
        }
    }

    private void send(String to, Message message) {
        Session recipient = connections.get(to);
        if (recipient != null) {
            try {
                recipient.getBasicRemote().sendText(messageConverter.convert(message));
            } catch (IOException e) {
                logger.error("Could not send message {} to session {}", message, to, e);
            }
        }
    }

    private void broadcast(Message message) {
        Set<String> recipients = subscriptionManager.getSubscribers(message.messageType);
        recipients.stream().forEach(s -> {
            try {
                connections.get(s).getBasicRemote().sendText(messageConverter.convert(message));
            } catch (IOException e) {
                logger.error("Could not send message {} to session {}", message, s, e);
            }
        });
    }

    private void registerInterestedEventListeners() {
        eventPublisher.addListener(AfterUserLoginEvent.class, new EventListener<AfterUserLoginEvent>(){
            @Override
            public void onEvent(AfterUserLoginEvent event) {
                broadcast(new Message(USER_LOGGED_IN, new User[]{event.user}));
                subscriptionManager.subscribe(event.sessionId, USER_LOGGED_IN, USER_LOGGED_OUT);
            }
        });

        eventPublisher.addListener(UserErrorEvent.class, new EventListener<UserErrorEvent>() {
            @Override
            public void onEvent(UserErrorEvent event) {
                send(event.sessionId, new Message(MessageType.USER_ERROR, event.getError()));
            }
        });

        eventPublisher.addListener(UserMessageEvent.class, new EventListener<UserMessageEvent>() {
            @Override
            public void onEvent(UserMessageEvent event) {
                send(event.sessionId, new Message(event.getMessageType(), event.getPayload()));
            }
        });

        eventPublisher.addListener(AfterUserLogoutEvent.class, new EventListener<AfterUserLogoutEvent>() {
            @Override
            public void onEvent(AfterUserLogoutEvent event) {
                subscriptionManager.unsubscribe(event.sessionId);
                broadcast(new Message(USER_LOGGED_OUT, new User[]{event.user}));
            }
        });

        eventPublisher.addListener(AfterGameSessionInitEvent.class, new EventListener<AfterGameSessionInitEvent>() {
            @Override
            public void onEvent(AfterGameSessionInitEvent event) {
                GameSession gameSession = event.getGameSession();
                send(gameSession.getGuest().getSessionId(), new Message(GAME_SESSION_INIT, gameSession));
            }
        });

        eventPublisher.addListener(AfterGameSessionAcceptEvent.class, new EventListener<AfterGameSessionAcceptEvent>() {
            @Override
            public void onEvent(AfterGameSessionAcceptEvent event) {
                Message message = new Message(GAME_SESSION_CREATED, event.getGameSession());
                send(event.getGameSession().getOwner().getSessionId(), message);
                send(event.getGameSession().getGuest().getSessionId(), message);
            }
        });

        eventPublisher.addListener(AfterGameSessionRejectEvent.class, new EventListener<AfterGameSessionAcceptEvent>() {
            @Override
            public void onEvent(AfterGameSessionAcceptEvent event) {
                GameSession gameSession = event.getGameSession();
                send(gameSession.getOwner().getSessionId(), new Message(GAME_SESSION_REJECTED, gameSession));
            }
        });

        eventPublisher.addListener(AfterGameSessionCloseEvent.class, new EventListener<AfterGameSessionCloseEvent>() {
            @Override
            public void onEvent(AfterGameSessionCloseEvent event) {
                GameSession gameSession = event.getGameSession();
                Message message = new Message(GAME_SESSION_REMOVED, gameSession);
                send(gameSession.getGuest().getSessionId(), message);
                send(gameSession.getOwner().getSessionId(), message);
            }
        });
    }

    private void initClientMessageHandlers() {

        clientMessageHandlerMap.put(USER_TRY_LOGIN, (clientSessionId, clientRequest) ->
                eventPublisher.publish(new UserLoginEvent(clientSessionId, (String) clientRequest.payload)));

        clientMessageHandlerMap.put(GAME_SESSION_INIT, (clientSessionId, clientRequest) ->
                eventPublisher.publish(new BeforeGameSessionInitEvent(clientSessionId, (String) clientRequest.payload)));

        clientMessageHandlerMap.put(GAME_SESSION_ACCEPTED, (clientSessionId, clientRequest) ->
                eventPublisher.publish(new BeforeGameSessionAcceptEvent(clientSessionId, (String) clientRequest.payload)));

        clientMessageHandlerMap.put(GAME_SESSION_REJECTED, (clientSessionId, clientRequest) ->
                eventPublisher.publish(new BeforeGameSessionRejectEvent(clientSessionId, (String) clientRequest.payload)));

        clientMessageHandlerMap.put(GAME_SESSION_CLOSE, (clientSessionId, clientRequest) ->
                eventPublisher.publish(new BeforeGameSessionCloseEvent(clientSessionId, (String) clientRequest.payload)));

    }

}
