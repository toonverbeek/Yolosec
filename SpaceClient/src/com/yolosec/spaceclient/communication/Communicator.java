/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.communication;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
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
import java.util.Iterator;

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

    public static final String IP_ADDRESS = "145.93.219.29";
   

    public static void sendData(String json) {
        writer.println(json);
    }

    public static List<GameObjectImpl> retrieveData() throws IOException {
        JsonReader jreader = new JsonReader(new InputStreamReader(socket.getInputStream()));
        jreader.setLenient(true);
        for (GamePacket gp : Serializer.deserializePackets(jreader)) {
            if (gp instanceof SpaceshipComm) {
                SpaceshipComm sc = (SpaceshipComm) gp;
                gameObjects.add(new Spaceship(sc));
            } else if (gp instanceof AsteroidComm) {
                AsteroidComm ac = (AsteroidComm) gp;
                System.out.println("Got new AsteroidComm: "  + ac.toString());
                gameObjects.add(new Asteroid(ac));
            }
        }
        return gameObjects;

    }

    public static void login(String json) {
        System.out.println("Sout login: " + json);
        writer.println(json);
    }

    public static boolean initiate() throws SocketException{
        try {
            System.out.println("-----Initializing Comm Link to Server");
            socket = new Socket(IP_ADDRESS, 1337);
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

}
