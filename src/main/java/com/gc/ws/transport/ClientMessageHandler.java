package com.gc.ws.transport;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
interface ClientMessageHandler {

    void handle(String clientSessionId, Message clientMessage);

}
