package com.gc.ws.transport;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/29/2015
 */
class Message {
    MessageType messageType;
    Object payload;

    Message(MessageType messageType, Object payload) {
        this.messageType = messageType;
        this.payload = payload;
    }

    Message() {
    }
}
