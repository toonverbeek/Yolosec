/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yolosec.spaceclient.communication;

import shared.LoginComm;
import shared.Serializer;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.AsteroidComm;
import com.yolosec.spaceclient.dao.interfaces.DrawCallback;
import com.yolosec.spaceclient.game.world.GameObjectImpl;
import shared.SpaceshipComm;
import com.yolosec.spaceclient.game.world.Asteroid;
import com.yolosec.spaceclient.game.player.Spaceship;
import com.yolosec.spaceclient.gui.SpaceClient;

/**
 *
 * @author Tim
 */
public class BroadcastHandler implements Runnable {

    private DrawCallback callBack;
   // private Communicator communicator;
    private List<GameObjectImpl> retrievedObjectList;
    private static final Gson gson = new Gson();

    public BroadcastHandler(DrawCallback callBack) {
        this.callBack = callBack;
    }

    @Override
    public void run() {
        //communicator = new Communicator();
        //communicator.initiate();
        login("asdf2", "asdf2");
        while (true) {
            handleData();
        }
    }

    public void login(String username, String password) {
        LoginComm lc = new LoginComm(LoginComm.class.getSimpleName(), username, password);
        Communicator.login(Serializer.serializeLogin(lc));

    }

    public void sendData(GameObjectImpl gObject) {
        String json = "";
        if (gObject instanceof Spaceship) {
            Spaceship s = (Spaceship) gObject;
            json = Serializer.serializeSpaceShipAsGamePacket(SpaceshipComm.class.getSimpleName(), s.getPosition().x, s.getPosition().y, 1, s.getId(), s.getResources());
        } else if (gObject instanceof Asteroid) {
            Asteroid asteroid = (Asteroid) gObject;
            json = Serializer.serializeAsteroidAsGamePacket(AsteroidComm.class.getSimpleName(), asteroid.getType(), asteroid.getResourceAmount(), (int) asteroid.getX(), (int) asteroid.getY());
        }
        if (!json.equals("")) {
            Communicator.sendData(json);
        }
    }

    private void handleData() {
        try {
            retrievedObjectList = Communicator.retrieveData();
            if (retrievedObjectList != null) {
                //GameObject sToAdd = Serializer.desirializePacket(gson.toJsonTree(retrievedJson));
                callBack.drawAfterDataReadFromSocketFromServer(retrievedObjectList);
            }
        } catch (IOException ex) {
            Logger.getLogger(SpaceClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
