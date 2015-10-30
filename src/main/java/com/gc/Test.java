package com.gc;

import org.glassfish.tyrus.server.Server;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Author: Gennadii Cherniaiev
 * Date: 10/29/2015
 */
@ServerEndpoint("/ws")
public class Test {
    //queue holds the list of connected clients
    private static Queue<Session> queue = new ConcurrentLinkedQueue<Session>();
    private static Thread rateThread ; //rate publisher thread
    static
    {
//rate publisher thread, generates a new value for USD rate every 2 seconds.
        rateThread=new Thread(){
            public void run() {
                DecimalFormat df = new DecimalFormat("#.####");
                while(true)
                {
                    double d=2+Math.random();
                    if(queue!=null)
                        sendAll("USD Rate: "+df.format(d));
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                    }
                }
            };
        } ;
        rateThread.start();
    }

    @OnOpen
    public void open(Session session) {
        queue.add(session);
        System.out.println("New session opened: "+session.getId());
    }
    @OnError
    public void error(Session session, Throwable t) {
        queue.remove(session);
        System.err.println("Error on session "+session.getId());
    }
    @OnClose
    public void closedConnection(Session session) {
        queue.remove(session);
        System.out.println("session closed: "+session.getId());
    }

    @OnMessage
    public void onMessage(Session session, String msg) {
//provided for completeness, in out scenario clients don't send any msg.
        try {
            System.out.println("received msg "+msg+" from "+session.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        runServer();
    }

    public static void runServer() {
        Server server = new Server("localhost", 8025, "/websockets", Test.class);

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


    private static void sendAll(String msg) {
        try {
   /* Send the new rate to all open WebSocket sessions */
            ArrayList<Session > closedSessions= new ArrayList<>();
            for (Session session : queue) {
                if(!session.isOpen())
                {
                    System.err.println("Closed session: "+session.getId());
                    closedSessions.add(session);
                }
                else
                {
                    session.getBasicRemote().sendText(msg);
                }
            }
            queue.removeAll(closedSessions);
            System.out.println("Sending " + msg + " to " + queue.size() + " clients");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
