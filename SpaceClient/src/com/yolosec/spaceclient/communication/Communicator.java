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

    public static final String IP_ADDRESS = "145.93.58.174";

    public static void sendData(String json) {
        writer.println(json);
    }

    public static Socket getSocket() {
        return socket;
    }

    
    /*
      Retrieves 
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
                    }
                    gameObjects.add(spaceship); //always add, for now

                } else if (gp instanceof AsteroidComm) {
                    AsteroidComm ac = (AsteroidComm) gp;
                    Asteroid a = new Asteroid(ac);
                    gameObjects.add(a);
                }
            }
        }
        return gameObjects;

    }

    public static void sendLogin(String json) {
        System.out.println("login: " + json);
        writer.println(json);
    }
   
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
            return true;
        } catch (UnknownHostException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public static SpaceshipComm receiveLogin() throws IOException {
        SpaceshipComm spacecomm = null;
        while (spacecomm == null) {
            JsonReader jreader = new JsonReader(new InputStreamReader(Communicator.getSocket().getInputStream()));
            jreader.setLenient(true);
            try {
                System.out.println("----start receiving");
                GamePacket gp = Serializer.getSingleGamePacket(jreader);
                if (gp instanceof SpaceshipComm) {
                    spacecomm = (SpaceshipComm) gp;
                    System.out.println("----received");
                }
                System.out.println("----end receiving");
            } catch (IOException ex) {
                spacecomm = null;
            }
        }
        return spacecomm;
    }

}
