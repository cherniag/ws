package com.gc.ws;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/30/2015
 */
public interface MessageHandler {

    void handle(String from, ClientRequest clientRequest);
}
