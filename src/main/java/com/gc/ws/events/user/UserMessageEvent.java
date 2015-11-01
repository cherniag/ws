package com.gc.ws.events.user;

import com.gc.ws.events.Event;
import com.gc.ws.transport.MessageType;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
public class UserMessageEvent extends Event {

    private MessageType messageType;
    private Object payload;

    public UserMessageEvent(String sessionId, MessageType messageType, Object payload) {
        super(sessionId);
        this.messageType = messageType;
        this.payload = payload;
    }

    public Object getPayload() {
        return payload;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public String toString() {
        return "UserMessageEvent{" +
                "messageType=" + messageType +
                ", payload=" + payload +
                '}';
    }

}
