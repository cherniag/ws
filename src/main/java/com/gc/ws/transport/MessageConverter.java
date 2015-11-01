package com.gc.ws.transport;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/29/2015
 */
@Component
class MessageConverter {
    private Gson gson = new Gson();

    String convert(Message message) {
        return gson.toJson(message);
    }

    Message convert(String msg) {
        return gson.fromJson(msg, Message.class);
    }
}
