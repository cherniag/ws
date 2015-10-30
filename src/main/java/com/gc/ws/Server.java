package com.gc.ws;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/29/2015
 */
public class Server {
    public static void main(String[] args) {
        runServer();
    }

    public static void runServer() {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);

        org.glassfish.tyrus.server.Server server = new org.glassfish.tyrus.server.Server("localhost", 8026, "/websockets", GameEndpoint.class);

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
