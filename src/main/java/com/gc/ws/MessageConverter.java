package com.gc.ws;

import com.google.gson.Gson;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/29/2015
 */
public class MessageConverter {
    private Gson gson = new Gson();


    public String convert(ServerResponse serverResponse) {
        return gson.toJson(serverResponse);
    }

    public ClientRequest convert(String msg) {
        return gson.fromJson(msg, ClientRequest.class);
    }
}
