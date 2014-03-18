/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceclient.communication;

import shared.Serializer;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.geom.Vector2f;
import shared.AsteroidComm;
import shared.GameObject;
import shared.GamePacket;
import shared.SpaceshipComm;
import spaceclient.game.Asteroid;
import spaceclient.game.Spaceship;

/**
 *
 * @author Tim
 */
public class Communicator {

    private static Socket socket;
    private static PrintWriter writer;
    private static BufferedReader reader;
    private static Gson gson = new Gson();

    public static final String IP_ADDRESS = "127.0.0.1";
    public Communicator() {
        initiate();
    }

    public void sendData(String json) {
        writer.println(json);
    }

    public List<GameObject> retrieveData() throws IOException {
        JsonReader jreader = new JsonReader(new InputStreamReader(socket.getInputStream()));
        jreader.setLenient(true);
        ArrayList<GameObject> gameObjects = new ArrayList<>();
        for (GamePacket gp : Serializer.deserializePackets(jreader)) {
            if (gp instanceof SpaceshipComm) {
                SpaceshipComm sc = (SpaceshipComm) gp;
                gameObjects.add(new Spaceship(sc));
            } else if (gp instanceof AsteroidComm) {
                AsteroidComm ac = (AsteroidComm) gp;
                gameObjects.add(new Asteroid(ac));
            }
        }
        return gameObjects;

    }

    

    public void login(String json) {
        writer.println(json);
    }

    public boolean initiate() {
        try {
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
