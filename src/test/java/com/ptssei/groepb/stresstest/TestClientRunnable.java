package com.ptssei.groepb.stresstest;

import com.ptsesd.groepb.shared.LoginComm;
import com.ptsesd.groepb.shared.Serializer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.UnknownHostException;

/**
 *
 * @author user
 */
public class TestClientRunnable implements Runnable {

    private static Socket socket;
    private static PrintWriter writer;
    private static BufferedReader reader;
    private final String username;
    private final String IP_ADDRESS;

    public TestClientRunnable(String username, String ip) {
        this.username = username;
        this.IP_ADDRESS = ip;
        //System.out.println(username);
    }

    public boolean initiate() throws SocketException {
        try {
            System.out.println("-----Initializing Comm Link to Server");
            socket = new Socket(IP_ADDRESS, 1337);
            System.out.println("Connection successful");
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return true;
        } catch (UnknownHostException e) {
            System.out.println(String.format("Exception in TestClientRunnable.initiate() - %s", e.getMessage()));
            return false;
        } catch (IOException e) {
            System.out.println(String.format("Exception in TestClientRunnable.initiate() - %s", e.getMessage()));
            return false;
        }
    }

    @Override
    public void run() {
        //do work
        System.out.println("Trying to login user: " + username);
        //login first
//        Runnable receiver = new LoginListener();
//        Thread t = new Thread(receiver);
//        t.start();
        LoginComm lc = new LoginComm(LoginComm.class.getSimpleName(), username, username);
        String json = Serializer.serializeLogin(lc);
        writer.println(json);
    }
}
