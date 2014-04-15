/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.communication;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.ptsesd.groepb.shared.AsteroidComm;
import com.ptsesd.groepb.shared.GamePacket;
import com.ptsesd.groepb.shared.LoginCommError;
import com.ptsesd.groepb.shared.Serializer;
import com.ptsesd.groepb.shared.SpaceshipComm;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import com.yolosec.spaceclient.game.world.GameObjectImpl;
import com.yolosec.spaceclient.game.world.Asteroid;
import com.yolosec.spaceclient.game.player.Spaceship;

/**
 *
 * @author Tim
 */
public class Communicator {

    private static Socket socket;
    private static PrintWriter writer;
    private static BufferedReader reader;
    private static Gson gson = new Gson();
    private static ArrayList<GameObjectImpl> gameObjects = new ArrayList<>();

    public static final String IP_ADDRESS = "145.93.209.249";
    private static JsonReader jreader;

    public static void sendData(String json) {
        writer.println(json);
    }

    public static Socket getSocket() {
        return socket;
    }

    /**
     * Uses a jReader that is wrapped around a socket to listen for an incoming datastream.
     * The incoming data is segregated per type and a corresponding "COMM"(communication) class is constructed using this data.
     * The COMM class is finally added to a list and returned to the caller.
     * @param jreader which listens for a stream on the socket.
     * @return the list of COMM objects which were constructed with data read from the socket.
     * @throws Exception 
     */
    public static List<GameObjectImpl> retrieveData(JsonReader jreader) throws Exception {
        gameObjects = new ArrayList<>();
        if (jreader.hasNext()) {
            if (jreader.peek() == JsonToken.BEGIN_ARRAY) {
                List<GamePacket> packets = Serializer.deserializePackets(jreader);
                for (GamePacket gp : packets) {
                    if (gp instanceof SpaceshipComm) {
                        SpaceshipComm sc = (SpaceshipComm) gp;

                        gameObjects.add(new Spaceship(sc));
                    } else if (gp instanceof AsteroidComm) {
                        AsteroidComm ac = (AsteroidComm) gp;
                        gameObjects.add(new Asteroid(ac));
                    }
                }
            } else {
                GamePacket gp = Serializer.getSingleGamePacket(jreader);
                if (gp instanceof SpaceshipComm) {
                    SpaceshipComm sc = (SpaceshipComm) gp;
                    Spaceship spaceship = new Spaceship(sc);

                    if (gameObjects.contains(spaceship)) {
                    } else {
                        gameObjects.add(spaceship); //always add, for now
                    }

                } else if (gp instanceof AsteroidComm) {
                    AsteroidComm ac = (AsteroidComm) gp;
                    Asteroid a = new Asteroid(ac);
                    gameObjects.add(a);
                }
            }
        }
        return gameObjects;

    }

    /**
     * Sends a login request as json to the socket output stream.
     * @param json the json to be sent.
     */
    public static void sendLogin(String json) {
        System.out.println("login: " + json);
        writer.println(json);
    }

    /**
     * Initiates the socket and thus the connection the server.
     * @return true if the connection was successful, false otherwise.
     * @throws SocketException 
     */
    public static boolean initiate() throws SocketException {
        try {
            System.out.println("-----Initializing Comm Link to Server");
            socket = new Socket(IP_ADDRESS, 1337);
            System.out.println("Connection successful");
            //socket = new Socket("localhost", 1337);
            writer = new PrintWriter(socket.getOutputStream(),
                    true);
            reader = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));

            jreader = new JsonReader(new InputStreamReader(Communicator.getSocket().getInputStream()));
            jreader.setLenient(true);
            return true;
        } catch (UnknownHostException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Listens for a response to a login request.
     * @return the SpaceShipComm object that belongs to the login request, null if an exception occurred.
     * @throws Exception 
     */
    public static SpaceshipComm receiveLogin() throws Exception {
        SpaceshipComm spacecomm = null;
        while (spacecomm == null) {
            try {
                GamePacket gp = Serializer.getSingleGamePacket(jreader);
                if (gp instanceof SpaceshipComm) {
                    spacecomm = (SpaceshipComm) gp;
                } else if (gp instanceof LoginCommError) {
                    spacecomm = null;
                    break;
                }
            } catch (Exception ex) {
                spacecomm = null;
                ex.printStackTrace();
            }
        }
        return spacecomm;
    }

}
