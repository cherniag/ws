package com.gc.ws;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/29/2015
 */
public class ServerResponse {
    ResponseType responseType;
    Object payload;

    public ServerResponse(ResponseType responseType, Object payload) {
        this.responseType = responseType;
        this.payload = payload;
    }

    public ServerResponse() {
    }
}
