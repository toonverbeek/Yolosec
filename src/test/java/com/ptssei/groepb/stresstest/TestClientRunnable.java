package com.ptssei.groepb.stresstest;

import com.google.gson.stream.JsonReader;
import com.ptsesd.groepb.shared.LoginComm;
import com.ptsesd.groepb.shared.Serializer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * @author user
 */
public class TestClientRunnable implements Runnable {

    private static Socket socket;
    private static PrintWriter writer;
    private static BufferedReader reader;
    private static JsonReader jreader;

    private final String username;
    private final String IP_ADDRESS;

    public TestClientRunnable(String username, String ip) {
        this.username = username;
        this.IP_ADDRESS = ip;
        //System.out.println(username);
    }

    public boolean initiate() throws SocketException {
        try {
            //System.out.println("-----Initializing Comm Link to Server");
            socket = new Socket(IP_ADDRESS, 1337);
            //System.out.println("Connection successful");
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return true;
        } catch (Exception e) {
            System.out.println(String.format("Exception in TestClientRunnable.initiate() - %s", e.getMessage()));
        }
        return false;
    }

    @Override
    public void run() {
        //do work
        System.out.println("Trying to login user: " + username);
        //login first
        LoginComm lc = new LoginComm(LoginComm.class.getSimpleName(), username, username);
        String json = Serializer.serializeLogin(lc);
        writer.println(json);
//        try {
//            jreader = new JsonReader(new InputStreamReader(socket.getInputStream()));
//            jreader.setLenient(true);
//            boolean b = receiveLogin();
//            if (!b) {
//                System.out.println("Error while logging in user: " + username);
//            }
//        } catch (Exception e) {
//            System.out.println(String.format("Exception in TestClientRunnable.run() - %s", e.getMessage()));
//        }
    }

//    /**
//     * Listens for a response to a login request.
//     *
//     * @return the SpaceShipComm object that belongs to the login request, null
//     * if an exception occurred.
//     * @throws Exception
//     */
//    private boolean receiveLogin() throws Exception {
//        SpaceshipComm spacecomm = null;
//        while (spacecomm == null) {
//            try {
//                GamePacket gp = Serializer.getSingleGamePacket(jreader);
//                if (gp instanceof SpaceshipComm) {
//                    spacecomm = (SpaceshipComm) gp;
//                } else if (gp instanceof LoginCommError) {
//                    spacecomm = null;
//                    break;
//                }
//            } catch (Exception ex) {
//                spacecomm = null;
//                //ex.printStackTrace();
//            }
//        }
//        System.out.println("RETURN");
//        return (spacecomm != null);
//    }
}
