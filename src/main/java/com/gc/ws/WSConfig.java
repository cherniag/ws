package com.gc.ws;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Gennadii Cherniaiev
 * Date: 11/3/2015
 */
public class WSConfig implements ServerApplicationConfig {
    @Override
    public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned) {
       return scanned;
    }

    @Override
    public Set<ServerEndpointConfig> getEndpointConfigs(
            Set<Class<? extends Endpoint>> scanned) {
        // TODO Auto-generated method stub
        System.out.println("******getEndpointConfigs******" + scanned);
        Set<ServerEndpointConfig> res=new HashSet<>();
        return res;
    }
}
