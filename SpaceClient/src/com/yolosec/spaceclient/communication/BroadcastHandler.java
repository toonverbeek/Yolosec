/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.communication;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ptsesd.groepb.shared.AsteroidComm;
import com.ptsesd.groepb.shared.LoginComm;
import com.ptsesd.groepb.shared.Serializer;
import com.ptsesd.groepb.shared.SpaceshipComm;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.yolosec.spaceclient.dao.interfaces.DrawCallback;
import com.yolosec.spaceclient.game.world.GameObjectImpl;
import com.yolosec.spaceclient.game.world.Asteroid;
import com.yolosec.spaceclient.game.player.Spaceship;
import com.yolosec.spaceclient.gui.SpaceClient;
import java.io.InputStreamReader;

/**
 *
 * @author Tim
 */
public class BroadcastHandler implements Runnable {

    private DrawCallback callbackOwner;
    private List<GameObjectImpl> retrievedObjectList;
    private static final Gson gson = new Gson();

    /**
     * Creates a new instance of type BroadCastHandler. Listens for incoming
     * data from the GameServer. Also handles sending data to the GameServer.
     *
     * @param callbackOwner
     */
    public BroadcastHandler(DrawCallback callbackOwner) {
        this.callbackOwner = callbackOwner;
    }

    @Override
    public void run() {
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(Communicator.getSocket().getInputStream()));
            reader.setLenient(true);

            while (true) {
                handleData(reader);
            }
        } catch (Exception ex) {
            Logger.getLogger(BroadcastHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Sends a login request to the server.
     * @param username
     * @param password
     * @throws Exception 
     */
    public void login(String username, String password) throws Exception {
        LoginComm lc = new LoginComm(LoginComm.class.getSimpleName(), username, password);
        Communicator.sendLogin(Serializer.serializeLogin(lc));
    }

    
    /**
     * Sends a GameObjectImpl to the GameServer serialized as Json..
     * @param gObject the GameObjectImpl to send.
     */
    public void sendData(GameObjectImpl gObject) {
        String json = "";
        if (gObject instanceof Spaceship) {
            Spaceship s = (Spaceship) gObject;
            json = Serializer.serializeSpaceShipAsGamePacket(SpaceshipComm.class.getSimpleName(), s.getId(), s.getPosition().x, s.getPosition().y, 1, s.getResources(), s.isMining());
        } else if (gObject instanceof Asteroid) {
            Asteroid asteroid = (Asteroid) gObject;
            json = Serializer.serializeAsteroidAsGamePacket(AsteroidComm.class.getSimpleName(), asteroid.getType(), asteroid.getResourceAmount(), (int) asteroid.getXPosition(), (int) asteroid.getYPosition());
        }
        if (!json.equals("")) {
            Communicator.sendData(json);
        }
    }

    /**
     * Listens for incoming data and, when retrieved, passes it on to the CallBackOwner for processing.
     * @param reader
     * @throws Exception 
     */
    private void handleData(JsonReader reader) throws Exception {
        try {
            retrievedObjectList = Communicator.retrieveData(reader);
            if (retrievedObjectList != null) {
                //GameObject sToAdd = Serializer.desirializePacket(gson.toJsonTree(retrievedJson));
                callbackOwner.drawAfterDataReadFromSocketFromServer(retrievedObjectList);
            }
        } catch (IOException ex) {
            Logger.getLogger(SpaceClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
