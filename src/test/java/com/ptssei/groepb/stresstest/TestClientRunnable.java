/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ptssei.groepb.stresstest;

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
    
    public static final String IP_ADDRESS = "145.93.58.157‏";
    
    public TestClientRunnable(){
        
    }
    
    public boolean initiate() throws SocketException {
        try {
            System.out.println("-----Initializing Comm Link to Server");
            socket = new Socket(IP_ADDRESS, 1337);
            System.out.println("Connection successful");
            //socket = new Socket("localhost", 1337);
            writer = new PrintWriter(socket.getOutputStream(),true);
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
    }
}
