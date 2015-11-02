package com.gc.ws;

import com.gc.ws.transport.GameEndpoint;
import org.glassfish.tyrus.server.Server;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/29/2015
 */
public class Boot {
    public static void main(String[] args) {
        runServer();
    }

    public static void runServer() {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);

        Server server = new Server("localhost", 8026, "/websockets", new HashMap<>(), GameEndpoint.class);

        try {
            server.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Please press a key to stop the server.");
            reader.readLine();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            server.stop();
        }
    }
}
